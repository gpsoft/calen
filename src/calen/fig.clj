(ns calen.fig)

;; データ構造
;; fig: {:width 3, :height 2, :body ["a" "b"]}

(defn ->fig [w h b] {:width w, :height h, :body b})
(defn space [len] (apply str (repeat len " ")))
(defn vspace [n] (repeat n ""))
(defn wcount [s] (count (.getBytes s "Shift_JIS")))

(defn vertical
  "図形を上下に連結"
  [gap fig1 fig2]
  (let [w1 (:width fig1)
        w2 (:width fig2)
        h1 (:height fig1)
        h2 (:height fig2)
        b1 (:body fig1)
        b2 (:body fig2)]
    {:width (max w1 w2)
     :height (+ h1 gap h2)
     :body (concat b1 (vspace gap) b2)}))

(defn horizon
  "図形を水平に連結"
  [gap fig1 fig2]
  (let [w1 (:width fig1)
        w2 (:width fig2)
        h1 (:height fig1)
        h2 (:height fig2)
        b1 (concat (:body fig1) (vspace h2))
        b2 (concat (:body fig2) (vspace h1))
        h (max h1 h2)
        concat-with-gap
        (fn [l r]
          (str l (space (- w1 (wcount l))) (space gap) r))]
    {:width (+ w1 gap w2)
     :height h
     :body (->> (map concat-with-gap b1 b2)
                (take h))}))

(defn grid
  "複数の図形をグリッド上に並べて1つの図形にする"
  [figs numcols hgap vgap]
  (let [rows (partition numcols numcols nil figs)]
    (reduce (partial vertical vgap)
            (for [figs rows]
              (reduce (partial horizon hgap) figs)))))

(defn prn-fig
  "図形を印字"
  [{:keys [width height body]}]
  (doall
    (for [line body]
      (let [len (wcount line)
            padding (space (- width len))]
        (println (str line padding)))))
  nil)

(comment
  (prn-fig {:width 3, :height 2, :body ["a" "b"]})
  (-> (vertical
        2
        {:width 3, :height 2, :body ["a" "b"]}
        {:width 4, :height 3, :body ["a" "b" "c"]})
      (prn-fig))
  (-> (horizon
        2
        {:width 3, :height 2, :body ["a" "b"]}
        {:width 4, :height 3, :body ["a" "b" "c"]})
      (prn-fig))
  (grid [{:width 3, :height 2, :body ["a" "b"]}
         {:width 3, :height 2, :body ["a" "b"]}
         {:width 3, :height 2, :body ["a" "b"]}] 2 3 1)
  (reduce (partial horizon 3) [{:width 3, :height 2, :body ["a" "b"]}
         {:width 3, :height 2, :body ["a" "b"]}
         {:width 3, :height 2, :body ["a" "b"]}])
  )
