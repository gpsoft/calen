(ns calen.core
  (:require [calen.fig :as fig]))

;; 略語とデータ構造
;;
;; nyd: new year's day, 元日
;; bom: beginning of month, ついたち
;; dow: day of week, 曜日
;;
;; date: [year month day]
;; dow: 0-6, 0:Sunday, 1:Monday, ...,6:Saturday
;; ad1: [1 1 1], nyd of AD 1


;; 例えば2017年5月1日の曜日を得るには…
;; 　・元日の曜日
;;   ・各月の日数
;;   ・元日から各月の1日までの日数
;; などから計算

;; [31 28 31 30 31 30 31 31 30 31 30 31]  ...各月の日数
;; ↓
;; [0 31 59 90 120 151 ...]  ...元日からの日数
;; ↓
;; (元日の曜日 + 120) % 7



(def ad1 [1 1 1])  ;; 0001-01-01
(def dow-ad1 1)    ;; 0001-01-01は月曜
(def dow-names ["日" "月" "火" "水" "木" "金" "土"])

(defn- y-of [[y]] y)
(defn- m-of [[_ m]] m)
(defn- d-of [[_ _ d]] d)

(defn- dow-add
  "ある曜日(dow-base)の日から
  何日(num-days)か後の曜日"
  [dow-base num-days]
  (-> (+ dow-base num-days)
      (mod 7)))


(defn leap?
  "うるう年か?
  true for 2016, 2020, 2000, 2400
  false for 2017, 2100, 2200
  "
  [y]
  (if (zero? (mod y 100))
    (zero? (mod y 400))
    (zero? (mod y 4))))

(defn num-days-before
  "ad1から前年まで(ad1を含めて)何日あった?"
  [y]
  (->> (range 1 y)
         (map #(if (leap? %) 366 365))
         (reduce +)))
(defn num-days-before'
  "num-days-beforeの高速バージョン?"
  [y]
  (let [x (dec y)
        num-leap-years (+ (quot x 4)
                          (- (quot x 100))
                          (quot x 400))]
    (-> (* x 365)
        (+ num-leap-years))))

(defn dow-of-nyd
  "元日(new year's day)の曜日"
  [y]
  (let [ndb (num-days-before y)]
    (dow-add dow-ad1 ndb)))

(defn num-days-list
  "各月の日数のリスト
  [31 28 31 ...]
  "
  [y]
  [31 (if (leap? y) 29 28) 31 30
   31 30 31 31 30 31 30 31])

(defn num-days-before-list
  "年初から前月までの日数のリスト
  (0 31 59 90 ...)
  "
  [y]
  (let [ndl (num-days-list y)]
    (->> (reductions + 0 ndl)
         (take 12))))

(defn bom-dow-list
  "ついたちの曜日のリスト"
  [y]
  (let [downyd (dow-of-nyd y)]
    (->> (num-days-before-list y)
        (map #(dow-add downyd %)))))

(defn num-days
  "月の日数"
  [y m]
  (nth (num-days-list y) (dec m)))

(defn bom-dow
  "ついたちの曜日"
  [y m]
  (nth (bom-dow-list y) (dec m)))

(defn dow
  "曜日"
  [[y m d :as date]]
  (let [bomdow (bom-dow y m)]
    (dow-add bomdow (dec d))))

(defn dow-name
  "曜日名"
  [dow]
  (nth dow-names dow))



(defn render-ym
  "年月ラベル"
  [y m]
  (str y "年" m "月"))

(defn render-dows
  "曜日ラベル"
  [hgap]
  (clojure.string/join (fig/space hgap) dow-names))

(defn day->fig
  "1日分を図形化する"
  [d]
  (let [strd (if (pos? d) (format "%2d" d) "")]
    (fig/->fig 2 1 [strd])))

(defn month->fig
  "ひと月分を図形化する"
  [y m]
  (let [hgap 1
        vgap 0
        heading-dow (render-dows hgap)
        headings (fig/->fig
                   (count heading-dow) 2
                   [(render-ym y m) heading-dow])
        bomdow (bom-dow y m)
        nd (num-days y m)
        ds (map inc (range (- bomdow) nd))]
    (fig/vertical
      vgap
      headings
      (fig/grid (map day->fig ds) 7 hgap vgap))))

(defn year->fig
  "1年分を図形化する"
  [y numcols]
  (let [hgap 3
        vgap 1]
    (fig/grid
      (map #(month->fig y %) (range 1 13))
      numcols hgap vgap)))

(comment
  (fig/prn-fig (month->fig 2017 5))
  (fig/prn-fig (year->fig 2018 4))
  )
