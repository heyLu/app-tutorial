(ns client.rendering
  (:require [domina :as dom]
            [io.pedestal.app.render.push :as render]
            [io.pedestal.app.render.push.templates :as templates]
            [io.pedestal.app.render.push.handlers :as h]
            [io.pedestal.app.render.push.handlers.automatic :as d]
            [io.pedestal.app.render.events :as events])
  (:require-macros [client.html-templates :as html-templates]))

(def templates (html-templates/client-templates))

(defn add-template [renderer [_ path :as delta] input-queue]
  (let [parent (render/get-parent-id renderer path)
        id (render/new-id! renderer path)
        html (templates/add-template renderer path (:client-page templates))]
    (dom/append! (dom/by-id parent) (html {:id id}))
    (let [game (js/BubbleGame. "game-board")]
      (render/set-data! renderer path game)
      (dotimes [_ 5] (.addBubble game)))))

(defn game [renderer]
  (render/get-data renderer [:main]))

(defn destroy-game [renderer [_ path :as delta] input-queue]
  (.destroy (game renderer))
  (render/drop-data! renderer path)
  (h/default-destroy renderer delta input-queue))

(defn add-player [renderer [_ path] _]
  (.addPlayer (game renderer) (last path)))

(defn set-score [renderer [_ path _ score] _]
  (let [name (last path)
        game (game renderer)]
    (.setScore game name score)
    (when (not= name "Me") ; why only when not me?
      (.removeBubble game))))

(defn set-stat [renderer [_ path _ value] _]
  (let [stat (last path)]
    (if-let [game (game renderer)]
      (.setStat game (name stat) value))))

(defn set-player-order [renderer [_ path _ pos] _]
  (let [name (last path)]
    (.setOrder (game renderer) name pos)))

(defn add-handler [renderer [_ path transform-name messages] input-queue]
  (.addHandler (game renderer)
               (fn [points]
                 (events/send-transforms input-queue transform-name messages))))

(defn add-bubbles [renderer [_ path _ m] _]
  (dotimes [_ (:count m)]
    (.addBubble (game renderer))))

(defn render-config []
  [[:node-create [:main] add-template]
   [:node-destroy [:main] destroy-game]
   [:node-create [:main :counters :*] add-player]

   [:value [:main :counters :*] set-score]
   [:value [:main :debug :*] set-stat]
   [:value [:main :*] set-stat]
   [:value [:main :player-order :*] set-player-order]
   [:value [:main :add-bubbles] add-bubbles]

   [:transform-enable [:main :my-counter] add-handler]])
