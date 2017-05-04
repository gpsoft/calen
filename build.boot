(def project 'calen)
(def version "1.0.0")
(def github "https://github.com/gpsoft/calen")

;; The project skeleton was created by
;; $ boot -d boot/new new -t app -n calen

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(task-options!
  aot {:namespace   #{'calen.main}}
  pom {:project     project
       :version     version
       :description "Print calendar"
       :url         github
       :scm         {:url github}
       :license     {"Eclipse Public License"
                     "http://www.eclipse.org/legal/epl-v10.html"}}
  jar {:main        'calen.main
       :file        (str "calen-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[calen.main :as app])
  (apply (resolve 'app/-main) args))

(require '[adzerk.boot-test :refer [test]])
