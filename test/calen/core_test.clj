(ns calen.core-test
  (:require [clojure.test :refer :all]
            [calen.core :refer :all]))

(deftest not-leap-test
  (is (not (leap? 2017))))
(deftest normal-leap-test
  (is (leap? 2016)))
(deftest special-not-leap-test
  (is (not (leap? 2100))))
(deftest special-leap-test
  (is (leap? 2000)))

(deftest dow-2017-test
  (is (= 0 (dow-of-nyd 2017))))
(deftest dow-2018-test
  (is (= 1 (dow-of-nyd 2018))))

(deftest dow-of-bom-20170101-test
  (is (= 0 (bom-dow 2017 1))))
(deftest dow-of-bom-20170201-test
  (is (= 3 (bom-dow 2017 2))))
(deftest dow-of-bom-20171201-test
  (is (= 5 (bom-dow 2017 12))))

(deftest dow-ad1-test
  (is (= 1 (dow [1 1 1]))))
(deftest dow-20170503-test
  (is (= 3 (dow [2017 5 3]))))

(comment
  (require '[calen.core :refer :all] :reload-all)
  (num-days-list 2017)
  (num-days-before-list 2017)
  (dow-of-nyd 2017)
  (num-days-before 2)
  (num-days-before 2017)
  (num-days-before' 2)
  (num-days-before' 2017)
  (bom-dow 2017 2)
  (bom-dow-list 2017)
  (dow-name (dow [2017 5 3]))
  (dow-name (dow [2017 5 3]))
  )
