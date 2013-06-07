(ns tutorial-client.simulated.services
  (:require [io.pedestal.app.protocols :as p]
            [io.pedestal.app.messages :as msg]
            [io.pedestal.app.util.platform :as platform]))

(def counters (atom {"abc" 0 "xyz" 0}))

(defn increment-counter [key t app]
  (p/put-message (:input app) {msg/type :swap
                               msg/topic [:other-counters key]
                               :value (get (swap! counters update-in [key] inc) key)})
  (platform/create-timeout t #(increment-counter key t app)))

(defn receive-messages [app]
  (increment-counter "abc" 2000 app)
  (increment-counter "xyz" 5000 app))

(defrecord MockServices [app]
  p/Activity
  (start [this]
    (receive-messages app))
  (stop [this]))
