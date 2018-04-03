(require '[clojure.edn :as edn])

(def project {:project     'com.grzm/ex.boot-javac
              :description "Some example"
              :url         "https://github.com/grzm/ex.boot-javac"
              :scm         {:url "https://github.com/grzm/ex.boot-javac"}
              :license     {"MIT" "https://opensource.org/licenses/MIT"}})

(def version {:major 0, :minor 0, :incremental 1, :qualifier "SNAPSHOT"})

(defn format-version [v]
  (let [{:keys [major minor incremental]} v]
    (if-let [qualifier (:qualifier v)]
      (format "%d.%d.%d-%s" major minor incremental qualifier)
      (format "%d.%d.%d" major minor incremental))))

(def version-string (format-version version))

(set-env! :dependencies '[[adzerk/boot-test "RELEASE" :scope "test"]
                          [metosin/boot-alt-test "0.3.2" :scope "test"]
                          [seancorfield/boot-tools-deps "0.3.0" :scope "test"
                           :exclusions [ch.qos.logback/logback-classic
                                        org.clojure/clojure]]])

(require '[boot-tools-deps.core :refer [deps load-deps]])

(def pom-options (assoc project :version version-string))

(deftask p-v
  "display project and version"
  []
  (prn :pom pom-options))

(task-options!
  pom (constantly (assoc project :version version-string)))

(deftask build
  "Build and install the project locally."
  []
  (comp (deps) (pom) (javac) (jar) (install)))

;;; testing

(require '[adzerk.boot-test :as boot-test])

(deftask test
  []
  (comp (deps :aliases [:test] :overwrite-boot-deps true)
        (javac)
        (boot-test/test)))

(require '[metosin.boot-alt-test :as boot-alt-test])

(deftask alt-test
  []
  (comp (deps :aliases [:test]
              :quick-merge true)
        (watch)
        (javac)
        (boot-alt-test/alt-test)))
