(ns client.html-templates
  (:use [io.pedestal.app.templates :only [tfn dtfn tnodes]]))

(defmacro client-templates
  []
  {:client-page (dtfn (tnodes "client.html" "tutorial" [[:#other-counters]]) #{:id})
   :other-counter (dtfn (tnodes "client.html" "other-counter") #{:id})})
