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

(defn init-main [_]
  ; tells the renderer that a user action related to :my-counter can
  ; send {msg/type :inc msg/topic [:my-counter]} to increment the
  ; counter
  [[:transform-enable [:main :my-counter] :inc [{msg/topic [:my-counter]}]]])

(def example-app
  {:version 2
              ; type topic (routed to first match in order)
   :transform [[:inc  [:my-counter] inc-transform]
               [:swap [:**]         swap-transform]]
   :effect #{[#{[:my-counter]} publish-counter :single-val]}
   :derive #{[#{[:my-counter] [:other-counters :*]} [:total-count] total-count :vals]}
   :emit [{:init init-main}
          [#{[:my-counter]
             [:other-counters :*]
             [:total-count]} (app/default-emitter [:main])]]})

