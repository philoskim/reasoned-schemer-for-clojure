(ns rs.ch1
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]))

;; Chapter 1: Playthings

; 1.6
s#
; => #function[clojure.core.logic/succeed]


; 1.8
u#   ; the abbreviation of unsuccessful
; => #function[clojure.core.logic/fail]


; 1.10
(run* [q]
  u#)
; => ()

; the same as before
(run false [q]
  u#)
; => ()


; 1.11
(run* [q]
  (== true q))
; => (true)


; 1.12
(run* [q]
  u#
  (== true q))
; => ()


; 1.13
(run* [q]
  s#
  (== true q))
; => (true)


; 1.15
(run* [r]
  s#
  (== 'corn r))
; => (corn)


; 1.17
(run* [r]
  u#
  (== 'corn r))
; => ()


; 1.18
(run* (q)
  s#
  (== false q))
; => (false)


; 1.19
; (== false x)
; => CompilerException java.lang.RuntimeException: Unable to resolve symbol: x in this context


; 1.20
(let [x true]
  (== false x))
; => #function[clojure.core.logic/==/fn--15821]


; 1.21
(let [x false]
  (== false x))
; => #function[clojure.core.logic/==/fn--15821]


; 1.22
(run* [x]
  (let [x false]
    (== true x)))
; => ()


; 1.23
(run* [q]
  (fresh [x]
    (== true x)
    (== true q)))
; => (true)


; 1.26
(run* [q]
  (fresh [x]
    (== x true)
    (== true q)))
; => (true)


; 1.27
(run* [q]
  (fresh [x]
    (== x true)
    (== q true)))
; => (true)


; 1.28
(run* [x]
  s#)
; => (_0)


; 1.29
(run* [x]
  (let [x false]
    (fresh [x]
      (== true x))))
; => (_0)


; 1.30
(run* [r]
  (fresh [x y]
    (== (lcons x (lcons y ())) r)))
; => ((_0 _1))


; 1.31
(run* [s]
  (fresh [t u]
    (== (lcons t (lcons u ())) s)))
; => ((_0 _1))


; 1.32
(run* [r]
  (fresh [x]
    (let [y x]
      (fresh [x]
        (== (lcons y (lcons x (lcons y ()))) r)))))
; => ((_0 _1 _0))


; 1.33
(run* [r]
  (fresh [x]
    (let [y x]
      (fresh [x]
        (== (lcons x (lcons y (lcons x ()))) r)))))
; => ((_0 _1 _0))


; 1.34
(run* [q]
  (== false q)
  (== true q))
; => ()


; 1.35
(run* [q]
  (== false q)
  (== false q))
; => (false)


; 1.36
(run* [q]
  (let [x q]
    (== true x)))
; => (true)


; 1.37
(run* [r]
  (fresh [x]
    (== x r)))
; => (_0)


; 1.38
(run* [q]
  (fresh [x]
    (== true x)
    (== x q)))
; => (true)


; 1.39
(run* [q]
  (fresh [x]
    (== x q)
    (== true x)))
; => (true)


; 1.40
(run* [q]
  (fresh [x]
    (== true x)
    (== x q)))
; => (true)

(run* [q]
  (fresh [x]
    (== (= x q) q)))
; => (false)

(run* [q]
  (let [x q]
    (fresh [q]
      (== (= x q) x))))
; => (false)


; 1.41
(cond
  false true
  :else false)
; => false


; 1.43
(cond
  false s#
  :else u#)
; => #function[clojure.core.logic/fail]


; 1.44
(conde
  [u# s#]
  [s# u#])
; => #function[logic-deom.reasoned.ch01/eval17028/fn--17029]


; 1.45
(conde
  [u# u#]
  [s# u#])
; => #function[logic-deom.reasoned.ch01/eval17019/fn--17020]


; 1.46
(conde
  [s# s#]
  [s# u#])
; => #function[logic-deom.reasoned.ch01/eval17039/fn--17040]


; 1.47
(run* [x]
  (conde
    [(== 'olive x) s#]
    [(== 'oil x) s#]
    [s# u#]))
; => (olive oil)


; 1.49
(run 1 [x]
  (conde
    [(== 'olive x) s#]
    [(== 'oil x) s#]
    [s# u#]))
; => (olive)


; 1.50
(run* [x]
  (conde
    [(== 'virgin x) u#]
    [(== 'olive x) s#]
    [s# s#]
    [(== 'oil x) s#]
    [s# u#]))
; => (olive _0 oil)

(run* [x]
  (conde
    [(== 'virgin x) u#]
    [(== 'olive x) s#]
    [s# s#]
    [(== 'oil x) s#]))
; => (olive _0 oil)


; 1.52
(run 2 [x]
  (conde
    [(== 'extra x) s#]
    [(== 'virgin x) s#]
    [(== 'olive x) s#]
    [s# s#]
    [(== 'oil x) s#]
    [s# u#]))
; => (extra virgin)


; 1.53
(run* [r]
  (fresh [x y]
    (conde
      [(== 'split x)
       (== 'pea y)
       (== (lcons x (cons y ())) r)])))
; => ((split pea))


; 1.54
(run* (r)
  (fresh [x y]
    (conde
      [(== 'split x) (== 'pea y)]
      [(== 'navy x) (== 'bean y)]
      [s# u#])
    (== (lcons x (lcons y ())) r)))
; => ((split pea) (navy bean))


; 1.55
(run* (r)
  (fresh [x y]
    (conde
      [(== 'split x) (== 'pea y)]
      [(== 'navy x) (== 'bean y)]
      [s# u#])
    (== (lcons x (lcons y (lcons 'soup ()))) r)))
; => ((split pea soup) (navy bean soup))


; 1.56
(defn teacupo [x]
  (conde
    [(== 'tea x) s#]
    [(== 'cup x) s#]
    [s# u#]))

(run* [x]
  (teacupo x))
; => (tea cup)


; 1.57
; The reason why the result order is different from the textbook is explained in
; https://github.com/clojure/core.logic/wiki/Differences-from-The-Reasoned-Schemer
(run* [r]
  (fresh [x y]
    (conde
      [(teacupo x) (== true y) s#]
      [(== false x) (== true y)]
      [s# u#])
    (== (lcons x (lcons y ())) r)))
; => ((false true) (tea true) (cup true))


; 1.58
(run* [r]
  (fresh [x y z]
    (conde
      [(== y x) (fresh (x) (== z x))]
      [(fresh (x) (== y x)) (== z x)]
      [s# u#])
    (== (lcons y (lcons z ())) r)))
; => ((_0 _1) (_0 _1))


; 1.59
(run* [r]
  (fresh [x y z]
    (conde
      [(== y x) (fresh (x) (== z x))]
      [(fresh (x) (== y x)) (== z x)]
      [s# u#])
    (== false x)
    (== (lcons y (lcons z ())) r)))
; => ((false _0) (_0 false))


; 1.60
(run* [q]
  (let [a (== true q)
        b (== false q)]
    b))
; => (false)


; 1.61
(run* [q]
  (let [a (== true q)
        b (fresh [x]
            (== x q)
            (== false x))
        c (conde
            [(== true q) s#])]
    b))
; => (false)
