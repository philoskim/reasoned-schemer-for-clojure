(ns rs.util
  (:refer-clojure :exclude [== >= <= > < =])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.arithmetic :refer [>= <= > < =]]))

(defn pair? [x]
   (or (lcons? x)
       (and (coll? x) (seq x))))

(defn pairo [p]
  (fresh [a d]
    (conso a d p)))

(defn listo [l]
  (conde
    [(emptyo l) s#]
    [(pairo l)
     (fresh [d]
       (resto l d)
       (listo d))]
    [s# u#]))
