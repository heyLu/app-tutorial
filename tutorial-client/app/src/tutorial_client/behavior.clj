(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.dataflow :as dataflow]
              [io.pedestal.app.messages :as msg]))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn swap-transform [_ message]
  (:value message))

(defn average-count [_ inputs]
  (let [nums (dataflow/input-vals inputs)]
    (/ (apply + nums) (count nums))))

(defn maximum [old-value inputs]
  (max (or old-value 0) (or (dataflow/single-val inputs) 0)))

(defn cumulative-average [debug inputs]
  (let [k (last (first (keys (dataflow/input-map inputs))))
        x (dataflow/single-val inputs)
        i (inc (or (::avg-count debug) 0))
        avg (or (::avg-raw debug) 0)
        new-avg (+ avg (/ (- x avg) i))]
    (assoc debug
      ::avg-count i
      ::avg-raw new-avg
      (keyword (str (name k) "-avg")) (int new-avg))))

(defn init-emitter [_]
  [[:transform-enable [:tutorial :my-counter] :inc [{msg/topic [:my-counter]}]]])

(defn publish-counter [inputs]
  [{msg/type :swap
    msg/topic [:other-counters]
    :value (dataflow/single-val inputs)}])

(def example-app
  {:version 2
   :debug true
   :transform [[:inc   [:my-counter]        inc-transform]
               [:swap  [:other-counters :*] swap-transform]
               [:debug [:pedestal :**]      swap-transform]]
   :derive #{[#{[:my-counter] [:other-counters :*]} [:average-count] average-count]
             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug :dataflow-time-max] maximum]
             [#{[:pedestal :debug :dataflow-time]} [:pedestal :debug] cumulative-average]}
   :effect #{[#{[:my-counter]} publish-counter]}
   :emit [{:in #{[:my-counter]
                 [:other-counters :*]
                 [:average-count]}
           :fn (app/default-emitter :tutorial)
           :init init-emitter}
          [#{[:pedestal :debug :dataflow-time]
             [:pedestal :debug :dataflow-time-max]
             [:pedestal :debug :dataflow-time-avg]} (app/default-emitter [])]]})
