(ns rs.ch4
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]
            [rs.util :as ut]))

(use 'debux.core)

;; Chapter 4: Members Only

; 4.1
(defn mem [x l]
  (cond
    (empty? l) false
    (= (first l) x) l
    :else (mem x (rest l))))

(mem 'tofu '(a b tofu d peas e))
; => (tofu d peas e)


; 4.2
(mem 'tofu '(a b peas d peas e))
; => false


; 4.3
(run* [out]
  (== (mem 'tofu '(a b tofu d peas e)) out))
; => ((tofu d peas e))


; 4.4
(mem 'peas
     (mem 'tofu '(a b tofu d peas e)))
; => (peas e)


; 4.5
(mem 'tofu
     (mem 'tofu '(a b tofu d tofu e)))
; => (tofu d tofu e)


; 4.6
(mem 'tofu
   (rest (mem 'tofu '(a b tofu d tofu e))))
; => (tofu e)


; 4.7
(defn memo [x l out]
  (conde
    [(emptyo l) u#]
    [(firsto l x) (== l out)]
    [(fresh [d]
       (resto l d)
       (memo x d out))]))


; 4.10
(run 1 [out]
  (memo 'tofu '(a b tofu d tofu e) out))
; => ((tofu d tofu e))


; 4.11
(run 1 [out]
  (fresh [x]
    (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)))
; => ((tofu d tofu e))


; 4.12
(run* [r]
  (memo r
        '(a b tofu d tofu e)
        '(tofu d tofu e)))
; => (tofu)


; 4.13
(run* [q]
  (memo 'tofu '(tofu e) '(tofu e))
  (== true q))
; => (true)


; 4.14
(run* [q]
  (memo 'tofu '(tofu e) '(tofu))
  (== true q))
; => ()


; 4.15
(run* [x]
  (memo 'tofu '(tofu e) (list x 'e)))
; => (tofu)


; 4.16
(run* [x]
  (memo 'tofu '(tofu e) (list 'peas x)))
; => ()


; 4.17
(run* [out]
  (fresh [x]
    (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out)))
; => ((tofu d tofu e) (tofu e))


; 4.18
(run 12 [z]
  (fresh [u]
    (memo 'tofu (llist 'a 'b 'tofu 'd 'tofu 'e z) u)))
; => (_0
;     _0
;     (tofu . _0)
;     (_0 tofu . _1)
;     (_0 _1 tofu . _2)
;     (_0 _1 _2 tofu . _3)
;     (_0 _1 _2 _3 tofu . _4)
;     (_0 _1 _2 _3 _4 tofu . _5)
;     (_0 _1 _2 _3 _4 _5 tofu . _6)
;     (_0 _1 _2 _3 _4 _5 _6 tofu . _7)
;     (_0 _1 _2 _3 _4 _5 _6 _7 tofu . _8)
;     (_0 _1 _2 _3 _4 _5 _6 _7 _8 tofu . _9))


; 4.21
(defn memo2 [x l out]
  (conde
    [(firsto l x) (== l out)]
    [(fresh [d]
       (resto l d)
       (memo2 x d out))]))


; 4.22
(defn rember [x l]
  (cond
    (empty? l)      ()
    (= x (first l)) (rest l)
    :else           (cons (first l)
                          (rember x (rest l)))))


; 4.23
(rember 'peas '(a b peas d peas e))
; => (a b d peas e)



; 4.24
; rembero already exists in clojure.core.logic, so renamed rembero1.
; rembero stands for remove membero.
(defn rembero1 [x l out]
  (conde
    [(emptyo l)   (== () out)]
    [(firsto l x) (resto l out)]
    [s#           (fresh [res]
                   (fresh [d]
                     (resto l d)
                     (rembero1 x d res))
                   (fresh [a]
                     (firsto l a)
                     (conso a res out)))]))
(run 1 [q]
  (rembero1 'peas '(a b peas d peas e) q))
; => ((a b d peas e))


; 4.25
(defn rembero2 [x l out]
  (conde
    [(emptyo l)   (== () out)]
    [(firsto l x) (resto l out)]
    [s#           (fresh [a d res]
                    (resto l d)
                    (rembero2 x d res)
                    (firsto l a)
                    (conso a res out))]))

(run 1 [q]
  (rembero2 'peas '(a b peas d peas e) q))
; => ((a b d peas e))


; 4.26
(defn rembero30 [x l out]
  (conde
    [(emptyo l)   (== () out)]
    [(firsto l x) (resto l out)]
    [s#           (fresh [a d res]
                    (conso a d l)
                    (rembero3 x d res)
                    (conso a res out))]))

(defn rembero3 [x l out]
  (dbg ["0" x l out]) 
  (conde
    [(ut/t "1-1" l out)
     (emptyo l)   (== () out)
     (ut/t "1-2" l out)]
    [(ut/t "2-1" x out l)
     (firsto l x) (resto l out)
     (ut/t "2-2" x out l)]
    [s#           (fresh [a d res]
                    (conso a d l)
                    (ut/t "3-1" a d l)
                    (rembero3 x d res)
                    (ut/t "3-2" x d res)
                    (conso a res out)
                    (ut/t "3-3" a res out))]))

(run 1 [q]
  (rembero3 'peas '(a b peas d peas e) q))
; => ((a b d peas e))


; 4.30
(run 1 [out]
  (fresh [y]
    (rembero 'peas (list 'a 'b y 'd 'peas 'e) out)))
; => ((a b d peas e))


; 4.31
(run* [out]
  (fresh [y z]
    (rembero3 y (list 'a 'b y 'd z 'e) out)))
; => ((b a d _0 e)
;     (a b d _0 e)
;     (a b d _0 e)
;     (a b d _0 e)
;     (a b _0 d e)
;     (a b e d _0)
;     (a b _0 d _1 e))


; 4.49
(run* [r]
  (fresh [y z]
    (rembero3 y (list y 'd z 'e) (list y 'd 'e))
    (== (list y z) r)))
; => ((d d) (d d) (_0 _0) (e e))


; 4.57
(run 1 [w]
  (fresh [y z out]
    (rembero3 y (llist 'a 'b y 'd z w) out)))
; => (_0
;     _0
;     _0
;     _0
;     _0
;     ()
;     (_0 . _1)
;     (_0)
;     (_0 _1 . _2)
;     (_0 _1)
;     (_0 _1 _2 . _3)
;     (_0 _1 _2)
;     (_0 _1 _2 _3 . _4))


; 4.68
(defn surpriseo [s]
  (rembero3 s '(a b c) '(a b c)))


; 4.69
(run* [r]
  (== 'd r)
  (surpriseo r))
; => (d)


; 4.70
(run* [r]
  (surpriseo r))
; => (_0)


; 4.71
(run* [r]
  (surpriseo r)
  (== 'b r))
; => (b)


; 4.72
(run* [r]
  (== 'b r)
  (surpriseo r))
; => (b)
