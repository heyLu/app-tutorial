(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.dataflow :as dataflow]
              [io.pedestal.app.messages :as msg]))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn swap-transform [_ message]
  (:value message))

(defn init-emitter [_]
  [[:transform-enable [:tutorial :my-counter] :inc [{msg/topic [:my-counter]}]]])

(defn publish-counter [inputs]
  [{msg/type :swap
    msg/topic [:other-counters]
    :value (dataflow/single-val inputs)}])

(def example-app
  {:version 2
   :transform [[:inc [:my-counter] inc-transform]
               [:swap [:other-counters :*] swap-transform]]
   :effect #{[#{[:my-counter]} publish-counter]}
   :emit [{:in #{[:my-counter]
                 [:other-counters :*]}
           :fn (app/default-emitter :tutorial)
           :init init-emitter}]})
