(ns rs.ch4
  (:refer-clojure :exclude [== >= <= > < =])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.arithmetic :refer [>= <= > < =]]
            [rs.util :as ut]))

(use 'debux.core)

;; Chapter 4 : Members only

(defn mem [x l]
  (cond
    (empty? l) false
    (clojure.core/= (first l) x) l
    :else (mem x (rest l))))

(mem 'tofu (list 'a 'b 'peas 'd 'peas 'e))

(run* [out]
  (== (mem 'tofu (list 'a 'b 'tofu 'd 'peas 'e)) out))


(comment
  (defn memo [x l out]
    (conde
     [(emptyo l) u#]
     []))
)

(defn memo [x l out]
  (conde
    [(emptyo l) u#]
    [(firsto l x) (== l out)]
    [(fresh (d)
       (resto l d)
       (memo x d out))]))


(run 1 [out]
  (fresh (y)
    (rembero 'peas (list 'a 'b y 'd 'peas 'e) out)))

(run 1 [out]
  (memo 'tofu (list 'a 'b 'tofu 'd 'tofu 'e) out))

(run 1 [out]
  (fresh [x]
    (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)))

(run* [r]
  (memo r (list 'a 'b 'tofu 'd 'tofu 'e)
        (list 'tofu 'd 'tofu 'e)))

(run 1 [q]
  (memo 'tofu (list 'tofu 'e) (list 'tofu 'e))
  (== q true))

(run 1 [q]
  (memo 'tofu (list 'tofu 'e) (list 'tofu))
  (== q true))

(run 1 [x]
  (memo 'tofu (list 'tofu 'e) (list x 'e)))

(run 1 [x]
  (memo 'tofu (list 'tofu 'e) (list 'peas x)))

(run* [out]
  (fresh [x] (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)))

(run 12 [z]
  (fresh (u) (memo 'tofu (llist 'a 'b 'tofu 'd 'tofu 'e z) u)))

(defn memo2 [x l out]
  (conde
    [(firsto l x) (== l out)]
    [(fresh (d)
       (resto l d)
       (memo x d out))]))

(run 12 [z]
  (fresh (u) (memo2 'tofu (llist 'a 'b 'tofu 'd 'tofu 'e z) u)))


(defn rember [x l]
  (cond
    (empty? l) '()
    (clojure.core/= (first l) x) (rest l)
    :else
    (cons (first l)
          (rember x (rest l)))))

(rember 'peas (list 'a 'b 'peas 'd 'peas 'e))

(defn rembero [x l out]
  (conde
    [(emptyo l) (== '() out)]
    [(firsto l x) (resto l out)]
    [(fresh (res)
       (fresh (d)
         (resto l d)
         (rembero x d res))
       (fresh (a)
         (firsto l a)
         (conso a res out)))]))

(defn rembero [x l out]
  (conde
    [(emptyo l) (== '() out)]
    [(firsto l x) (resto l out)]
    [(fresh (a d res)
       (resto l d)
       (rembero x d res)
       (firsto l a)
       (conso a res out))]))

(defn rembero [x l out]
  (conde
    [(emptyo l) (== '() out)]
    [(firsto l x) (resto l out)]
    [(fresh (a d res)
       (conso a d l)
       (rembero x d res)
       (conso a res out))]))

(run 1 [out]
  (fresh (y)
    (rembero 'peas (list 'a 'b y 'd 'peas 'e) out)))

(run* [out]
  (fresh (y z)
    (rembero y (list 'a 'b y 'd z 'e) out)))

(run* [r]
  (fresh (y z)
    (rembero y (list y 'd z 'e) (list y 'd 'e))
    (== (list y z) r)))

(run* [w]
  (fresh (y z out)
    (rembero y (llist 'a 'b y 'd z w) out)))


