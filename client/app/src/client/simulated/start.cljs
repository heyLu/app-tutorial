(ns client.simulated.start
  (:require [io.pedestal.app.render.push.handlers.automatic :as d]
            [client.start :as start]
            ;; This needs to be included somewhere in order for the
            ;; tools to work.
            [io.pedestal.app-tools.tooling :as tooling]

            [io.pedestal.app :as app]
            [io.pedestal.app.protocols :as p]
            [client.simulated.services :as services]))

(defn ^:export main []
  (let [app (start/create-app d/data-renderer-config)
        services (services/->MockServices (:app app))]
    (app/consume-effects (:app app) services/services-fn)
    (p/start services)
    app))
