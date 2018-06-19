(ns rs.ch2
  (:require [clojure.core.logic :refer :all]))

;; Chapter 2: Teaching Old Toys New Tricks

; 2.1
(let [x (fn [a] a)
      y 'c]
  (x y))
; => c

; 2.2
(run* [r]
  (fresh [y x]
    (== (list x y) r)))
; => ((_0 _1))

; 2.3
(run* [r]
  (fresh [v w]
    (== (let [x v
              y w]
          (list x y))
        r)))
; => ((_0 _1))

; 2.4
(first '(grape raisin pear))
; => grape

; 2.5
(first '(a c o r n))
; => a

; 2.6
(run* [r]
  (firsto '(a c o r n) r))
; => (a)

; 2.7
(run* [r]
  (firsto '(a c o r n) 'a)
  (== true r))
; => (true)

; 2.8
(run* [r]
  (fresh [x y]
    (firsto (list r y) x)
    (== 'pear x)))
; => (pear)

; 2.9
; (defn caro [p a]
;   (fresh [d]
;     (== (lcons a d) p)))

; 2.10
(cons (first '(grape raisin pair))
      (first '((a) (b) (c))))
; => (grape a)

; 2.11
(run* [r]
  (fresh [x y]
    (firsto '(grape raisin pear) x)
    (firsto '((a) (b) (c)) y)
    (== (lcons x y) r)))
; => ((grape a))

; 2.13
(rest '(grape raisin pear))
; => (raisin pear)

; 2.14
(first (rest '(a c o r n)))
; => c

; 2.15
(run* [r]
  (fresh [v]
    (resto '(a c o r n) v)
    (firsto v r)))
; => (c)

; 2.16
; (defn cdro [p d]
;   (fresh [a]
;     (== (lcons a d) p)))

; 2.17
(cons
  (rest '(grape raisin pear))
  (first '((a) (b) (c))))
; => ((raisin pear) a)

; 2.18
(run* [r]
  (fresh [x y]
    (resto '(grape raisin pear) x)
    (firsto '((a) (b) (c)) y)
    (== (lcons x y) r)))
; => (((raisin pear) a))

; 2.19
(run* [q]
  (resto '(a c o r n) '(c o r n))
  (== true q))
; => (true)

; 2.20
(run* [x]
  (resto '(c o r n) (list x 'r 'n)))
; => (o)

; 2.21
(run* [l]
  (fresh [x]
    (resto l '(c o r n))
    (firsto l x)
    (== 'a x)))
; => ((a c o r n))

; 2.22
(run* [l]
  (conso '(a b c) '(d e) l))
; => (((a b c) d e))

; 2.23
(run* [x]
  (conso x '(a b c) '(d a b c)))
; => (d)

; 2.24
(run* [r]
  (fresh [x y z]
    (== (list 'e 'a 'd x) r)
    (conso y (list 'a z 'c) r)))
; => ((e a d c))

; 2.25
(run* [x]
  (conso x (list 'a x 'c) (list 'd 'a x 'c)))
; => (d)

; 2.26
(run* [l]
  (fresh [x]
    (== (list 'd 'a x 'c) l)
    (conso x (list 'a x 'c) l)))
; => ((d a d c))

; 2.27
(run* [l]
  (fresh [x]
    (conso x (list 'a x 'c) l)
    (== (list 'd 'a x 'c) l)))
; => ((d a d c))

; 2.28
; (defn conso [a d p]
;   (== (lcons a d) p))

; 2.29
(run* [l]
  (fresh [d x y w s]
    (conso w '(a n s) s)
    (resto l s)
    (firsto l x)
    (== 'b x)
    (resto l d)
    (firsto d y)
    (== 'e y)))
; => ((b e a n s))

; 2.30
(empty? '(grape rain pear))
; => false

; 2.31
(empty? ())
; => true

; 2.32
(run* [q]
  (emptyo '(grape raisin pear))
  (== true q))
; => ()

; 2.33
(run* [q]
  (emptyo ())
  (== true q))
; => (true)

; 2.34
(run* [x]
  (emptyo x))
; => (())

; 2.35
; (defn nullo [x]
;   (== x ()))

; 2.36
(= 'pear 'plum)
; => false

; 2.37
(= 'plum 'plum)
; => true

; 2.40
(defn eqo [x y]
  (== x y))

; 2.38
(run* [q]
  (eqo 'pear 'plum)
  (== true q))
; => ()

; 2.39
(run* [q]
  (eqo 'plum 'plum)
  (== true q))
; => (true)

; 2.41
; An utility function
; Refer to https://github.com/clojure/core.logic/wiki/Differences-from-The-Reasoned-Schemer
(defn pair? [x]
  (or (lcons? x)
      (and (coll? x) (seq x))))

(pair? (llist 'split 'pea))
; => true

; 2.42
(pair? (llist 'split 'x))
; => true

; 2.43
(pair? (llist '(split) 'pea))
; => true

; 2.44
(pair? ())
; => nil

; 2.45
(pair? 'pair)
; => false

; 2.46
(pair? 'pear)
; => false

; 2.47
(pair? '(pear))
; => (pear)

(pair? (llist 'pear ()))
; => (pear)

; 2.48
(first '(pear))
; => pear

; 2.49
(rest '(pear))
; => ()

; 2.51
(lcons '(split) 'pea)
; => ((split) . pea)

; 2.52
(run* [r]
  (fresh [x y]
    (== (lcons x (lcons y 'salad)) r)))
; => ((_0 _1 . salad))

; 2.53
(defn pairo [p]
  (fresh [a d]
    (conso a d p)))

; 2.54
(run* [q]
  (pairo (lcons q q))
  (== true q))
; => (true)

; 2.55
(run* [q]
  (pairo ())
  (== true q))
; => ()

; 2.56
(run* [q]
  (pairo 'pair)
  (== true q))
; => ()

; 2.57
(run* [r]
  (pairo r))
; => ((_0 . _1))

; 2.58
(run* [r]
  (pairo (lcons r 'pear)))
; => (_0)

