(ns client.services
  (:require [io.pedestal.app.protocols :as p]
            [cljs.reader :as reader]))

(defn receive-sse-event [app ev]
  (let [message (reader/read-string (.-data ev))]
    (p/put-message (:input app) message)))

(defrecord Services [app]
  p/Activity
  (start [this]
    (let [source (js/EventSource. "/msgs")]
      (.addEventListener source
                         "msg"
                         (partial receive-sse-event app)
                         false)))
  (stop [this]))

(defn services-fn [message input-queue]
  (let [body (pr-str message)]
    (let [http (js/XMLHttpRequest.)]
      (.open http "POST" "/msgs" true)
      (.setRequestHeader http "Content-Type" "application/edn")
      (.send http body))))
