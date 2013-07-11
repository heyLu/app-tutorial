{:config {:name :client, :description "Simple for testing the ui", :order 0}
 :data
 [
  [:node-create [] :map]
  [:node-create [:main] :map]
  [:node-create [:main :my-counter] :map]
  [:value [:main :my-counter] nil 1]
  [:transform-enable [:main :my-counter] :inc [{:io.pedestal.app.messages/topic [:my-counter]}]]
  [:node-create [:main :average-count] :map]
  [:value [:main :average-count] nil 3.33]
  [:node-create [:main :max-count] :map]
  [:value [:main :max-count] nil 6]
  [:node-create [:main :total-count] :map]
  [:value [:main :total-count] nil 10]
  [:node-create [:main :other-counters] :map]
  [:node-create [:main :other-counters "abc"] :map]
  [:value [:main :other-counters "abc"] nil 6]
  [:node-create [:main :other-counters "xyz"] :map]
  [:value [:main :other-counters "xyz"] nil 3]
  :break
  [:value [:main :other-counters "abc"] 6 7]
  [:value [:main :average-count] 3.33 3.67]
  [:value [:main :max-count] 6 7]
  [:value [:main :total-count] 10 11]
  :break
  [:value [:main :other-counters "xyz"] 3 4]
  [:value [:main :average-count] 3.67 4]
  [:value [:main :total-count] 11 12]
  :break
  [:value [:main :my-counter] 1 2]
  [:value [:main :average-count] 4 4.33]
  [:value [:main :total-count] 12 13]
  :break
  [:value [:main :my-counter] 2 3]
  [:value [:main :average-count] 4.33 4.67]
  [:value [:main :total-count] 13 14]
  :break
  [:value [:main :other-counters "abc"] 7 8]
  [:value [:main :average-count] 4.67 5]
  [:value [:main :max-count] 7 8]
  [:value [:main :total-count] 14 15]
  :break
  [:value [:main :my-counter] 3 4]
  [:value [:main :average-count] 5 8]
  [:value [:main :total-count] 15 16]
  :break
  [:value [:main :my-counter] 4 5]
  [:value [:main :average-count] 8 5.67]
  [:value [:main :total-count] 16 17]
  :break
  [:value [:main :my-counter] 5 6]
  [:value [:main :average-count] 5.67 6]
  [:value [:main :total-count] 17 18]
  :break
  [:value [:main :my-counter] 6 7]
  [:value [:main :average-count] 6 6.33]
  [:value [:main :total-count] 18 19]
  :break
  [:value [:main :other-counters "abc"] 8 9]
  [:value [:main :average-count] 6.33 6.67]
  [:value [:main :max-count] 8 9]
  [:value [:main :total-count] 19 20]
  :break
  [:value [:main :other-counters "abc"] 9 10]
  [:value [:main :average-count] 6.67 7]
  [:value [:main :max-count] 9 10]
  [:value [:main :total-count] 20 21]
  :break
  [:value [:main :other-counters "xyz"] 4 5]
  [:value [:main :average-count] 7 7.33]
  [:value [:main :total-count] 21 22]
  :break
  [:value [:main :my-counter] 7 8]
  [:value [:main :average-count] 7.33 7.67]
  [:value [:main :total-count] 22 23]
  :break
  [:value [:main :my-counter] 8 9]
  [:value [:main :average-count] 7.67 8]
  [:value [:main :total-count] 23 24]
  :break
  [:value [:main :other-counters "abc"] 10 11]
  [:value [:main :average-count] 8 8.33]
  [:value [:main :max-count] 10 11]
  [:value [:main :total-count] 24 25]
  :break
  [:value [:main :my-counter] 9 10]
  [:value [:main :average-count] 8.33 8.67]
  [:value [:main :total-count] 25 26]
  :break
  [:value [:main :other-counters "abc"] 11 12]
  [:value [:main :average-count] 8.67 9]
  [:value [:main :max-count] 11 12]
  [:value [:main :total-count] 26 27]
  :break
  [:value [:main :my-counter] 10 11]
  [:value [:main :average-count] 9 9.33]
  [:value [:main :total-count] 27 28]
  :break
  [:value [:main :my-counter] 11 12]
  [:value [:main :average-count] 9.33 14.5]
  [:value [:main :total-count] 28 29]
  :break
  [:value [:main :my-counter] 12 13]
  [:value [:main :average-count] 14.5 10]
  [:value [:main :max-count] 12 13]
  [:value [:main :total-count] 29 30]
  :break
  [:value [:main :other-counters "xyz"] 5 6]
  [:value [:main :average-count] 10 10.33]
  [:value [:main :total-count] 30 31]
  :break
  [:value [:main :other-counters "abc"] 12 13]
  [:value [:main :average-count] 10.33 16]
  [:value [:main :total-count] 31 32]
 ]}