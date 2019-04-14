(defproject minigame "1.0"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.8.1"]
                 [figwheel "0.5.16"]]

  :plugins [[lein-figwheel "0.5.16"]
            [lein-cljsbuild "1.1.7"]]

  :clean-targets ^{:protect false} ["target" "resources/public/js/compiled"]

  :cljsbuild {:builds [{; To run in development mode:
                        ; lein do clean, trampoline figwheel dev
                        :id "dev"
                        :source-paths ["src-cljs/"]
                        :figwheel true
                        :compiler {:optimizations :none
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :main "minigame.core"}}

                       {; To make a production build:
                        ; lein do clean, cljsbuild once prod
                        :id "prod"
                        :source-paths ["src-cljs/"]
                        :figwheel false
                        :compiler {:optimizations :advanced
                                   :output-to "resources/public/js/compiled/app.js"}}]})
