(ns ^:shared client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.messages :as msg]))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn swap-transform [_  message]
  (:value message))

(defn publish-counter [count]
  "Sends out `count` to a service.

The id will be set by that service before forwarding it to the other
clients."
  [{msg/type :swap msg/topic [:other-counters] :value count}])

(defn total-count [_ nums] (apply + nums))

(defn maximum [old-value nums]
  (apply max (or old-value 0) nums))

(defn average-count [_ {:keys [total nums]}]
  (/ total (count nums)))

(defn cumulative-average [debug key x]
  (let [k (last key)
        i (inc (or (::avg-count debug) 0))
        avg (or (::avg-raw debug) 0)
        new-avg (+ avg (/ (- x avg) i))]
    (assoc debug
           ::avg-count i
           ::avg-raw new-avg
           (keyword (str (name k) "-avg")) (int new-avg))))

(defn merge-counters [_ {:keys [me others]}]
  (assoc others "Me" me))

(defn sort-players [_ players]
  (into {} (map-indexed (fn [i [k v]] [k i])
                        (reverse
                          (sort-by second (map (fn [[k v]] [k v]) players))))))

(defn add-bubbles [_ {:keys [clock players]}]
  {:clock clock :count (count players)})

(defn init-main [_]
  ; tells the renderer that a user action related to :my-counter can
  ; send {msg/type :inc msg/topic [:my-counter]} to increment the
  ; counter
  [[:transform-enable [:main :my-counter] :inc [{msg/topic [:my-counter]}]]])

(def example-app
  {:version 2
   :debug true
              ; type topic (routed to first match in order)
   :transform [[:inc   [:*]            inc-transform]
               [:swap  [:**]           swap-transform]
               [:debug [:pedestal :**] swap-transform]]
   :effect #{[#{[:my-counter]} publish-counter :single-val]}
   :derive #{[#{[:counters :*]} [:total-count] total-count :vals]
             [#{[:counters :*]} [:max-count] maximum :vals]
             [{[:counters :*] :nums [:total-count] :total} [:average-count] average-count :map]
             [#{[:counters]} [:player-order] sort-players :single-val]
             ; :add-bubbles is computed by add-bubbles with ":clock as ; :clock"
             ; and ":counters as :players" as input map
             [{[:clock] :clock [:counters] :players} [:add-bubbles] add-bubbles :map]

             [{[:my-counter] :me [:other-counters] :others} [:counters] merge-counters :map]

             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug :dataflow-time-max] maximum :vals]
             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug] cumulative-average :map-seq]}
   :emit [{:init init-main}
          [#{[:total-count]
             [:max-count]
             [:average-count]} (app/default-emitter [:main])]
          [#{[:counters :*]} (app/default-emitter [:main])]
          [#{[:add-bubbles]} (app/default-emitter [:main])]
          [#{[:player-order :*]} (app/default-emitter [:main])]
          [#{[:clock]} (app/default-emitter [:main])]

          [#{[:pedestal :debug :dataflow-time]
             [:pedestal :debug :dataflow-time-max]
             [:pedestal :debug :dataflow-time-avg]} (app/default-emitter [])]]})
