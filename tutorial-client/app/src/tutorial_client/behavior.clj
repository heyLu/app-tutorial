(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.dataflow :as dataflow]
              [io.pedestal.app.messages :as msg]))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn swap-transform [_ message]
  (:value message))

(defn total-count [_ nums]
  (apply + nums))

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

(defn init-emitter [_]
  [[:transform-enable [:tutorial :my-counter] :inc [{msg/topic [:my-counter]}]]])

(defn publish-counter [count]
  [{msg/type :swap msg/topic [:other-counters] :value count}])

(def example-app
  {:version 2
   :debug true
   :transform [[:inc   [:my-counter]        inc-transform]
               [:swap  [:other-counters :*] swap-transform]
               [:debug [:pedestal :**]      swap-transform]]
   :derive #{[#{[:my-counter] [:other-counters :*]} [:total-count]
              total-count :vals]
             [{[:my-counter] :nums [:other-counters :*] :nums [:total-count] :total}
              [:average-count] average-count :map]
             [#{[:my-counter] [:other-counters :*]} [:max-count]
              maximum :vals]
             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug :dataflow-time-max]
              maximum :vals]
             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug]
              cumulative-average :map-seq]}
   :effect #{[#{[:my-counter]} publish-counter :single-val]}
   :emit [{:in #{[:my-counter]
                 [:other-counters :*]
                 [:average-count]
                 [:total-count]
                 [:max-count]}
           :fn (app/default-emitter :tutorial)
           :init init-emitter}
          [#{[:pedestal :debug :dataflow-time]
             [:pedestal :debug :dataflow-time-max]
             [:pedestal :debug :dataflow-time-avg]} (app/default-emitter [])]]})
