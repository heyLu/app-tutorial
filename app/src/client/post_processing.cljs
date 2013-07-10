(ns client.post_processing)

(defn- round [n places]
  (let [p (Math.pow 10 places)]
    (/ (Math.round (* n p)) p)))

(defn- round-number-post [[op path n]]
  [[op path (round n 2)]])

(defn add-post-processors [dataflow]
  (-> dataflow
      (update-in [:post :app-model] (fnil conj [])
                 [:value [:main :average-count] round-number-post])))
