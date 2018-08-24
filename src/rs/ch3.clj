(ns rs.ch3
  (:refer-clojure :exclude [== identity])
  (:require [clojure.core.logic :refer :all]
            [rs.util :as ut]))

(use 'debux.core)

;; Chapter 3: Seeing Old Friends in New Ways

; 3.1
; The reason why seq? is used instead of list? is explained in
; https://github.com/clojure/core.logic/wiki/Differences-from-The-Reasoned-Schemer
(seq? '((a) (a b) c))
; => true


; 3.2
(seq? ())
; => true


; 3.3
(seq? 's)
; => false


; 3.4
(seq? (llist 'd 'a 't 'e 's))
; => false


; 3.5
(defn listo [l]
  (conde
    [(emptyo l) s#]
    [(ut/pairo l)
     (fresh [d]
       (resto l d)
       (listo d))]
    [s# u#]))


; 3.6
(fresh (d)
  (resto 'l d)
  (listo d))
; => #function[rs.ch3/eval18141/fn--18142]


; 3.7
(run* [x]
  (listo (list 'a 'b x 'd)))
; => (_0)


; 3.10
(run 1 [x]
  (listo (llist 'a 'b 'c x)))
; => (())


; 3.13
; Don't run this one. It's an infinite loop
; (run 5 [x]
;   (listo (llist 'a 'b 'c x)))


; 3.14
(run 5 [x]
  (listo (llist 'a 'b 'c x)))
; => (() (_0) (_0 _1) (_0 _1 _2) (_0 _1 _2 _3))


; 3.16
; lol? stands for list-of-lists?
(defn lol? [l]
  (cond
    (empty? l) true
    (seq? (first l)) (lol? (rest l))
    :else false))

(lol? '((1 2 3) (4 5 6)))
; => true


; 3.17
(defn lolo [l]
  (conde
    [(emptyo l) s#]
    [(fresh [a]
       (firsto l a)
       (listo a))
     (fresh [b]
       (resto l b)
       (lolo b))]
    [s# u#]))


; 3.20
(run 1 [l]
  (lolo l))
; => (())


; 3.21
(run* [q]
  (fresh [x y]
    (lolo (list '(a b) (list x 'c) (list 'd y)))
    (== true q)))
; => (true)


; 3.22
(run 1 [q]
  (fresh [x y]
    (lolo (llist '(a b) x))
    (== true q)))
; => (true)


; 3.23
(run 1 [x]
  (lolo (llist '(a b) '(c d) x)))
; => (())


; 3.24
(run 10 [x]
  (lolo (llist '(a b) '(c d) x)))
; => (()
;     (())
;     ((_0))
;     (() ())
;     ((_0 _1)))


; 3.25
(run 5 [x]
  (lolo (llist '(a b) '(c d) '(() () () ()))))
; => (_0)


; 3.31
(defn twinso [s]
  (fresh [x y]
    (conso x y s)
    (conso x () y)))


; 3.32
(run* [q]
  (twinso '(tofu tofu))
  (== true q))
; => (true)


; 3.33
(run* [z]
  (twinso (list z 'tofu)))
; => (tofu)


; 3.36
(defn twinso2 [s]
  (fresh [x]
    (== (list x x) s)))

(run* (z)
  (twinso2 (list z 'tofu)))
; => (tofu)


; 3.37
; lot stands for list-of-twins
(defn loto [l]
  (conde
    [(emptyo l) s#]
    [(fresh [a]
       (firsto l a)
       (twinso a))
     (fresh [d]
       (resto l d)
       (loto d))]
    [s# u#]))


; 3.38
(run 1 [z]
  (loto (llist '(g g) z)))
; => (())


; 3.42
(run 5 [z]
  (loto (llist '(g g) z)))
; => (()
;     ((_0 _0))
;     ((_0 _0) (_1 _1))
;     ((_0 _0) (_1 _1) (_2 _2))
;     ((_0 _0) (_1 _1) (_2 _2) (_3 _3)))


; 3.45
(run 5 [r]
  (fresh [w x y z]
    (loto (llist '(g g) (list 'e w) (list x y) z))
    (== (list w (list x y) z) r)))
; => ((e (_0 _0) ())
;     (e (_0 _0) ((_1 _1)))
;     (e (_0 _0) ((_1 _1) (_2 _2)))
;     (e (_0 _0) ((_1 _1) (_2 _2) (_3 _3)))
;     (e (_0 _0) ((_1 _1) (_2 _2) (_3 _3) (_4 _4))))


; 3.47
(run 3 [out]
  (fresh [w x y z]
    (== (llist '(g g) (list 'e w) (list x y) z) out)
    (loto out)))
; => (((g g) (e e) (_0 _0))
;     ((g g) (e e) (_0 _0) (_1 _1))
;     ((g g) (e e) (_0 _0) (_1 _1) (_2 _2)))


; 3.48
(defn listofo [predo l]
  (conde
    [(emptyo l) s#]
    [(fresh [a]
       (firsto l a)
       (predo a))
     (fresh [d]
       (resto l d)
       (listofo predo d))]
    [s# u#]))


; 3.49
(run 3 [out]
  (fresh [w x y z]
    (== (llist '(g g) (list 'e w) (list x y) z) out)
    (listofo twinso out)))
; => (((g g) (e e) (_0 _0))
;     ((g g) (e e) (_0 _0) (_1 _1))
;     ((g g) (e e) (_0 _0) (_1 _1) (_2 _2)))


; 3.50
(defn loto2 [l]
  (listofo twinso l))


; 3.51
(defn eq-car? [l x]
  (= (first l) x))

(defn member? [x l]
  (cond
    (nil? l)      false
    (eq-car? l x) true
    :else         (member? x (next l))))


; 3.53
(member? 'olive '(virgin olive oil))
; => true


; 3.54
(defn eq-caro [l x]
  (firsto l x))

; membero already exists in core.logic.
(defn membero2 [x l]
  (conde
    [(emptyo l) u#]
    [(eq-caro l x) s#]
    [s# (fresh [d]
          (resto l d)
          (membero2 x d))]))


; 3.57
(run* [q]
  (membero2 'olive '(virgin olive oil))
  (== true q))
; => (true)


; 3.58
(run 1 [y]
  (membero2 y '(hummus with pita)))
; => (hummus)


; 3.59
(run 1 [y]
  (membero2 y '(with pita)))
; => (with)


; 3.60
(run 1 [y]
  (membero2 y '(pita)))
; => (pita)


; 3.61
(run 1 [y]
  (membero2 y ()))
; => ()


; 3.62
(run* [y]
  (membero2 y '(hummus with pita)))
; => (hummus with pita)


; 3.65
(defn identity [l]
  (run* [y]
    (membero2 y l)))

(identity '(a b c))
; => (a b c)


; 3.66
(run* [x]
  (membero2 'e (list 'pasta x 'fagioli)))
; => (e)


; 3.69
(run 1 [x]
  (membero2 'e (list 'pasta 'e x 'fagioli)))
; => (_0)


; 3.70
(run 1 [x]
  (membero2 'e (list 'pasta x 'e 'fagioli)))
; => (e)


; 3.71
(run* [r]
  (fresh (x y)
    (membero2 'e (list 'pasta x 'fagioli y))
    (== (list x y) r)))
; => ((e _0) (_0 e))


; 3.73
(run 1 [l]
  (membero2 'tofu l))
; => ((tofu . _0))


; 3.74
; (run* [l]
;   (membero2 'tofu l))


; 3.76
(run 5 [l]
  (membero2 'tofu l))
; => ((tofu . _0)
;     (_0 tofu . _1)
;     (_0 _1 tofu . _2)
;     (_0 _1 _2 tofu . _3)
;     (_0 _1 _2 _3 tofu . _4))


; 3.80
; pmembero may stand for membero of proper list.
(defn pmembero [x l]
  (conde
    [(emptyo l) u#]
    [(eq-caro l x) (resto l ())]
    [(fresh (d)
       (resto l d)
       (pmembero x d))]))

(run 5 [l]
  (pmembero 'tofu l))
; => ((tofu)
;     (_0 tofu)
;     (_0 _1 tofu)
;     (_0 _1 _2 tofu)
;     (_0 _1 _2 _3 tofu))


; 3.81
(run* [q]
  (pmembero 'tofu '(a b tofu d tofu))
  (== true q))
; => (true)


; 3.83
(defn pmembero2 [x l]
  (conde
    [(emptyo l) u#]
    [(eq-caro l x) (resto l ())]
    [(eq-caro l x) s#]
    [s# (fresh (d)
          (resto l d)
          (pmembero2 x d))]))


; 3.84
(run* [q]
  (pmembero2 'tofu '(a b tofu d tofu))
  (== true q))
; => (true true true)


; 3.86
(defn pmembero3 [x l]
  (conde
    [(emptyo l) u#]
    [(eq-caro l x) (resto l ())]
    [(eq-caro l x)
     (fresh [a d]
       (resto l (llist a d)))]
    [s# (fresh [d]
          (resto l d)
          (pmembero3 x d))]))


; 3.88
(run* [q]
  (pmembero3 'tofu '(a b tofu d tofu))
  (== true q))
; => (true true)


; 3.89
(run 12 [l]
  (pmembero3 'tofu l))
; => ((tofu)
;     (tofu _0 . _1)
;     (_0 tofu)
;     (_0 tofu _1 . _2)
;     (_0 _1 tofu)
;     (_0 _1 tofu _2 . _3)
;     (_0 _1 _2 tofu)
;     (_0 _1 _2 tofu _3 . _4)
;     (_0 _1 _2 _3 tofu)
;     (_0 _1 _2 _3 tofu _4 . _5)
;     (_0 _1 _2 _3 _4 tofu)
;     (_0 _1 _2 _3 _4 tofu _5 . _6))


; 3.93
(defn pmembero4 [x l]
  (conde
    [(emptyo l) u#]
    [(eq-caro l x)
     (fresh [a d]
       (resto l (llist a d)))]
    [(eq-caro l x) (resto l ())]    
    [s# (fresh [d]
          (resto l d)
          (pmembero4 x d))]))


; 3.94
(run 12 [l]
  (pmembero4 'tofu l))
; => ((tofu _0 . _1)
;     (tofu)
;     (_0 tofu _1 . _2)
;     (_0 tofu)
;     (_0 _1 tofu _2 . _3)
;     (_0 _1 tofu)
;     (_0 _1 _2 tofu _3 . _4)
;     (_0 _1 _2 tofu)
;     (_0 _1 _2 _3 tofu _4 . _5)
;     (_0 _1 _2 _3 tofu)
;     (_0 _1 _2 _3 _4 tofu _5 . _6)
;     (_0 _1 _2 _3 _4 tofu))


; 3.95
(defn first-value [l]
  (run 1 [y]
    (membero2 y l)))


; 3.96
(first-value '(pasta e fagioli))
; => (pasta)


; 3.98
(defn memberrevo [x l]
  (conde
    [(emptyo l) u#]
    [s# (fresh [d]
          (resto l d)
          (memberrevo x d))]
    [s# (eq-caro l x)]))


; 3.100
;; not working as expected. Is it because of lack of else clause again?
;; Can anyone tell me how to simulate the else clause?
(run* [x]
  (memberrevo x '(pasta e fagioli)))
; => (pasta e fagioli)


; 3.101
(defn reverse-list [l]
  (run* [y]
    (memberrevo y l)))

;; not working
(reverse-list '(a b c))
; => (a b c)
