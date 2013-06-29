{:config {:name :test, :description "Test", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:greeting] :map]
  [:value [:greeting] nil "Hello World!"]
 ]}