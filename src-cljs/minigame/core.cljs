(ns minigame.core
  (:require [reagent.core :as reagent]))

(def levels [{:start [5 5]
              :goal [1 1]
              :walls []}
             {:start [4 4]
              :goal [1 2]
              :walls [[1 3] [2 3] [3 3] [4 3] [5 3] [6 3]]}
             {:start [5 5]
              :goal [8 8]
              :walls [[3 7] [2 8] [8 5] [4 4] [7 7] [8 9] [9 6] [2 2] [1 7]]}
             {:start [5 5]
              :goal [4 5]
              :walls []}])

(defonce state (reagent/atom {:level 0
                              :x (-> levels first :start first)
                              :y (-> levels first :start second)}))

(defn current-level []
  (get levels (:level @state)))

(defn wall? [x y]
  (or
    (= 0 x)
    (= 10 x)
    (= 0 y)
    (= 10 y)
    (some #{[x y]} (:walls (current-level)))))

(defn goal-achieved? []
  (and
    (= (:x @state) (-> (current-level) :goal first))
    (= (:y @state) (-> (current-level) :goal second))))

(defn cell-color [x y]
  (if (wall? x y)
    "black"
    (if (and (= (:x @state) x) (= (:y @state) y) (goal-achieved?))
      "yellow"
      (if (and (= (:x @state) x) (= (:y @state) y))
        "red"
        (if (and (= (-> (current-level) :goal first) x) (= (-> (current-level) :goal second) y))
          "green"
          "gray")))))

(defn page []
  [:div
   [:div {:style {:color "gray" :font-size "200%"}} "Level " (inc (:level @state))]
   [:table {:style {:border-collapse :collapse} :cellSpacing "0"}
    [:tbody (doall (for [y (range 0 11)]
                     [:tr {:key (str "tr-" y)}
                      (doall (for [x (range 0 11)]
                               [:td {:style {:background-color (cell-color x y)
                                             :width "44px"
                                             :height "44px"}
                                     :key (str "td-" x "-" y)}]))]))]]
   [:div {:style {:color "gray"}} "Move using ↑→↓←, reach the green square, press Space."]])

(defn move [axis direction]
  (let [new-x (if (= :x axis) (direction (:x @state)) (:x @state))
        new-y (if (= :y axis) (direction (:y @state)) (:y @state))]
    (if (not (wall? new-x new-y))
      (do
        (swap! state update-in [axis] direction)
        (move axis direction)))))

(defn go-to-next-level []
  (let [next-level (inc (:level @state))
        new-coords (:start (get levels next-level))]
    (reset! state {:level next-level :x (first new-coords) :y (second new-coords)})))

(defn handle-keydown [e]
  (case (.-keyCode e)
    38 (move :y dec)
    40 (move :y inc)
    37 (move :x dec)
    39 (move :x inc)
    32 (if (goal-achieved?) (go-to-next-level))
    nil))

(defn render []
  (reagent/render-component [page] (.getElementById js/document "app"))
  (.addEventListener js/document "keydown" handle-keydown))

(render)
