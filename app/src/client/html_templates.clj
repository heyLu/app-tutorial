(ns client.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro client-templates
  []
  {:client-page (dtfn (tnodes "client.html" "hello") #{:id})})
