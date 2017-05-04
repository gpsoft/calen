(ns calen.main
  (:require [calen.core :as calen]
            [calen.fig :as fig])
  (:gen-class))

;; bootで起動するには…
;; LC_ALL=ja_JP.utf8 boot run -a 2017 -a 5
;; LC_ALL=ja_JP.utf8 boot run -a 2017 -a -3

(defn show-usage []
  (println " Usage: calen YEAR [MONTH or NUMOFCOLUMNS(in negative integer)]")
  (println "    Ex: calen 2017 5")
  (println "        calen 2017 -3"))

(defn -main
  [& args]
  (when (nil? args)
    (show-usage)
    (System/exit 0))
  (let [[y mc] args
        y (Integer/valueOf y)
        mc (if (nil? mc) -4 (Integer/valueOf mc))]
    (fig/prn-fig
      (if (neg? mc)
        (calen/year->fig y (- mc))
        (calen/month->fig y mc)))))
