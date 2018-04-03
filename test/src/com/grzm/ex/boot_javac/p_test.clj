(ns com.grzm.ex.boot-javac.p-test
  (:require
   [clojure.test :refer [deftest is]])
  (:import (com.grzm.ex.boot_javac P)))

(deftest static-field-test
  (is (= "my-val" P/SOME_CONSTANT)))

(deftest pass-through-test
  (let [s "foo"]
    (is (= s (P/passThrough s)))))
