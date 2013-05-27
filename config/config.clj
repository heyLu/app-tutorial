(ns config
  (:require [net.cgrand.enlive-html :as html]
            [io.pedestal.app-tools.compile :as compile]))

(def configs
  {:pedestal-app-tutorial
   {:build {:watch-files (compile/html-files-in "app/templates")
            :triggers {:html [#"pedestal_app_tutorial/rendering.js"]}}
    :application {:generated-javascript "generated-js"
                  :default-template "application.html"
                  :output-root :public}
    :control-panel {:design {:uri "/design.html"
                             :name "Design"
                             :order 0}}
    :built-in {:render {:dir "pedestal-app-tutorial"
                        :renderer 'pedestal_app_tutorial.rendering
                        :logging? true
                        :order 2
                        :menu-template "tooling.html"}}
    :aspects {:data-ui {:uri "/pedestal-app-tutorial-data-ui.html"
                        :name "Data UI"
                        :order 1
                        :out-file "pedestal-app-tutorial-data-ui.js"
                        :main 'pedestal_app_tutorial.simulated.start
                        :recording? true
                        :logging? true
                        :output-root :tools-public
                        :template "tooling.html"}
              :development {:uri "/pedestal-app-tutorial-dev.html"
                            :name "Development"
                            :out-file "pedestal-app-tutorial-dev.js"
                            :main 'pedestal_app_tutorial.start
                            :logging? true
                            :order 3}
              :fresh {:uri "/fresh.html"
                      :name "Fresh"
                      :out-file "fresh.js"
                      :main 'io.pedestal.app.net.repl_client
                      :order 4
                      :output-root :tools-public
                      :template "tooling.html"}
              :production {:uri "/pedestal-app-tutorial.html"
                           :name "Production"
                           :optimizations :advanced
                           :out-file "pedestal-app-tutorial.js"
                           :main 'pedestal_app_tutorial.start
                           :order 5}}}})
