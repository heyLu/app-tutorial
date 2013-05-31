(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.messages :as msg]))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn init-emitter [_]
  [[:transform-enable [:tutorial :my-counter] :inc [{msg/topic [:my-counter]}]]])

(def example-app
  {:version 2
   :transform [[:inc [:my-counter] inc-transform]]
   :emit [{:in #{[:*]} :fn (app/default-emitter :tutorial) :init init-emitter}]})
