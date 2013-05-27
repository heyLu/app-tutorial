(ns pedestal-app-tutorial.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro pedestal-app-tutorial-templates
  []
  {:pedestal-app-tutorial-page (dtfn (tnodes "pedestal-app-tutorial.html" "hello") #{:id})})
