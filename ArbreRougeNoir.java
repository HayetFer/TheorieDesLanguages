import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 *  erche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E>
 *            le type des clés stockées dans l'arbre
 */
public class ArbreRougeNoir<E> extends AbstractCollection<E> {
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;
    private final Noeud sentinelle = new Noeud(null);

    private class Noeud {
        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;
        Couleur couleur;

        Noeud(E cle) {
            this.cle = cle;
            gauche = sentinelle;
            droit = sentinelle;
        }

        /**
         * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné
         * dans ce noeud
         *
         * @return le noeud contenant la clé minimale du sous-arbre enraciné
         *         dans ce noeud
         */
        Noeud minimum() {
            Noeud actuel = this;
            while (actuel.gauche != sentinelle ) {
                actuel = actuel.gauche;
            }

            return actuel;
        }

        /**
         * Renvoie le successeur de ce noeud
         *
         * @return le noeud contenant la clé qui suit la clé de ce noeud dans
         *         l'ordre des clés, null si c'es le noeud contenant la plus
         *         grande clé
         */
        Noeud suivant() {
            if (droit != sentinelle) {
                return droit.minimum(); // Si le nœud a un sous-arbre droit, le successeur sera le nœud avec la clé
                                        // minimale de ce sous-arbre droit.
            } else {
                Noeud actuel = this;
                Noeud pereActuel = pere;

                // Tant que le nœud est le fils droit de son parent, on remonte dans l'arbre
                while (pereActuel != sentinelle && actuel == pereActuel.droit) {
                    actuel = pereActuel;
                    pereActuel = pereActuel.pere;
                }

                // Si le nœud n'est pas le fils droit de son parent, alors le parent est le
                // successeur
                return pereActuel;
            }
        }
    }

    // Consructeurs

    /**
     * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
     */
    public ArbreRougeNoir() {
        sentinelle.couleur = Couleur.Noir;
        racine = sentinelle;
        this.taille = 0;
    }

    /**
     * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
     * le comparateur
     *
     * @param cmp
     *            le comparateur utilisé pour définir l'ordre des éléments
     */
    public ArbreRougeNoir(Comparator<? super E> cmp) {
        sentinelle.couleur = Couleur.Noir;
        this.cmp = cmp;
        racine = sentinelle;
        this.taille = 0;
    }
    
    public ArbreRougeNoir(Collection<? extends E> c) {
        sentinelle.couleur=Couleur.Noir;
        racine=sentinelle;
        cmp = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
        Iterator<? extends E> iterator = c.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }

    public boolean ajout(Noeud z) {
        Noeud y = sentinelle;
        Noeud x = racine;
        
        while (x != sentinelle) {
            y = x;
            x = cmp.compare(z.cle, x.cle) < 0 ? x.gauche : x.droit;
        }
    
        z.pere = y;
        if (y == sentinelle) { // Tree is empty
            racine = z;
        } else {
            if (cmp.compare(z.cle, y.cle) < 0)
                y.gauche = z;
            else
                y.droit = z;
        }
    
        z.gauche = z.droit = sentinelle;
        z.couleur = Couleur.Rouge;
        ajouterCorrection(z);
		taille+=1;
        return true;
    }

    private void ajouterCorrection(Noeud z) {
        while (z.pere.couleur == Couleur.Rouge) {
            if (z.pere == z.pere.pere.gauche) {
                Noeud y = z.pere.pere.droit; // l'oncle de z
                if (y.couleur == Couleur.Rouge) {
                    // cas 1
                    z.pere.couleur = Couleur.Noir;
                    y.couleur = Couleur.Noir;
                    z.pere.pere.couleur = Couleur.Rouge;
                    z = z.pere.pere;
                } else {
                    if (z == z.pere.droit) {
                        // cas 2
                        z = z.pere;
                        rotationGauche(z);
                    }
                    // cas 3
                    z.pere.couleur = Couleur.Noir;
                    z.pere.pere.couleur = Couleur.Rouge;
                    rotationDroite(z.pere.pere);
                }
            } else {
                Noeud y=z.pere.pere.gauche;
                if(y.couleur == Couleur.Rouge){
                    z.pere.couleur = Couleur.Noir;
                    y.couleur = Couleur.Noir;
                    z.pere.pere.couleur = Couleur.Rouge;
                    z=z.pere.pere;
                }
                else{
                    if(z == z.pere.gauche){
                        z = z.pere;
                        rotationDroite(z);
                    }
                    z.pere.couleur = Couleur.Noir;
                    z.pere.pere.couleur = Couleur.Rouge;
                    rotationGauche(z.pere.pere);
                }
            }
        }
        racine.couleur = Couleur.Noir; // Property (2)
    }
     public Noeud rechercher(E cle) {
        Noeud current = racine;
        while (current != sentinelle && !current.cle.equals(cle)) {
            int comparisonResult = cmp.compare(cle, current.cle);
            if (comparisonResult < 0) {
                current = current.gauche;
                } else {
                    current = current.droit;
                }
            }
            if (current == sentinelle) {
                return null; // Node with the given key not found
            }
            return current; // Node with the given key found
        }
    public Noeud supprimer(Noeud z) {
        Noeud y;
        Noeud x;
        z=rechercher(z.cle);
        if (z.gauche == sentinelle || z.droit == sentinelle){
            y = z;
          }else{
            y = z.suivant();
          // y est le nœud à détacher
        }

          if (y.gauche != sentinelle){
             x = y.gauche;
          }else{
            x = y.droit;
          // x est le fils unique de y ou la sentinelle si y n'a pas de fils
        }
          x.pere = y.pere; // inconditionnelle

          if (y.pere == sentinelle) { // suppression de la racine
            racine = x;
          } else {
            if (y == y.pere.gauche){
              y.pere.gauche = x;
            }else{
              y.pere.droit = x;
          }
        }
          if (y != z){
           z.cle = y.cle;
           }
          if (y.couleur == Couleur.Noir){
          supprimerCorrection(x);
          }
		  taille-=1;
          return y.suivant();  
          
        }


        
public void supprimerCorrection(Noeud x) {
    Noeud w;
    while (x != racine && x.couleur == Couleur.Noir) {
    // (*) est vérifié ici
    if (x == x.pere.gauche) {
      w = x.pere.droit; // le frère de x
      if (w.couleur == Couleur.Rouge) {
        // cas 1
        w.couleur = Couleur.Noir;
        x.pere.couleur = Couleur.Rouge;
        rotationGauche(x.pere);
        w = x.pere.droit;
      }
      if (w.gauche.couleur == Couleur.Noir && w.droit.couleur == Couleur.Noir) {
        // cas 2
        w.couleur = Couleur.Rouge;
        x = x.pere;
      } else {
        if (w.droit.couleur == Couleur.Noir) {
          // cas 3
          w.gauche.couleur = Couleur.Noir;
          w.couleur = Couleur.Rouge;
          rotationDroite(w);
          w = x.pere.droit;
        }
        // cas 4
        w.couleur = x.pere.couleur;
        x.pere.couleur = Couleur.Noir;
        w.droit.couleur = Couleur.Noir;
        rotationGauche(x.pere);
        x = racine;
      }
    } else {
    // mimoir  
      w = x.pere.gauche;
      if (w.couleur == Couleur.Rouge) {
        // cas 1
        w.couleur = Couleur.Noir;
        x.pere.couleur = Couleur.Rouge;
        rotationDroite(x.pere);
        w = x.pere.gauche;
      }
      if (w.gauche.couleur == Couleur.Noir && w.droit.couleur == Couleur.Noir) {
        // cas 2
        w.couleur = Couleur.Rouge;
        x = x.pere;
      } else {
        if (w.gauche.couleur == Couleur.Noir) {
          // cas 3
          w.droit.couleur = Couleur.Noir;
          w.couleur = Couleur.Rouge;
          rotationGauche(w);
          w = x.pere.gauche;
        }
       // cas 4
        w.couleur = x.pere.couleur;
        x.pere.couleur = Couleur.Noir;
        w.gauche.couleur = Couleur.Noir;
        rotationDroite(x.pere);
        x = racine;
      }
      // idem en miroir, gauche <-> droite
      // cas 1', 2', 3', 4'
      
    }
  }
  // (**) est vérifié ici
  x.couleur = Couleur.Noir;
}

    
    private void rotationDroite(Noeud x) {
        Noeud y = x.gauche;
        x.gauche = y.droit;
        if (y.droit != null && y.droit != sentinelle) { // Use '&&' instead of '||' for conditions
            y.droit.pere = x;
        }
        y.pere = x.pere;
        if (x.pere == null || x.pere == sentinelle) {
            racine = y;
        } else if (x == x.pere.droit) {
            x.pere.droit = y;
        } else {
            x.pere.gauche = y;
        }
        y.droit = x;
        x.pere = y;
    }
    
    private void rotationGauche(Noeud x) {
        Noeud y = x.droit;
        x.droit = y.gauche;
        if (y.gauche != null && y.gauche != sentinelle) { // Use '&&' instead of '||' for conditions
            y.gauche.pere = x;
        }
        y.pere = x.pere;
        if (x.pere == null || x.pere == sentinelle) {
            racine = y;
        } else if (x == x.pere.gauche) {
            x.pere.gauche = y;
        } else {
            x.pere.droit = y;
        }
        y.gauche = x;
        x.pere = y;
    }
    public boolean add(E element) {
        Noeud newNode = new Noeud(element);
        ajout(newNode);
        return true;
    }
    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    if (racine != sentinelle) {
        printTree(racine, "", true, sb);
    }
    return sb.toString();
}

private void printTree(Noeud node, String prefix, boolean isLeft, StringBuilder sb) {
    if (node != null ) {
        sb.append(prefix);
        sb.append(isLeft ? "├──" : "└──");
        sb.append(node.cle).append(" ").append(node.couleur).append("\n");

        printTree(node.gauche, prefix + (isLeft ? "│   " : "    "), true, sb);
        printTree(node.droit, prefix + (isLeft ? "│   " : "    "), false, sb);
    }
}


    // TODO : voir quelles autres méthodes il faut surcharger
    public static void main(String[] args) {
        // Create a collection (e.g., ArrayList) with some elements
        Collection<Integer> collection = new ArrayList<>();
        collection.add(5);
        collection.add(2);
        collection.add(8);
        collection.add(1);
        collection.add(6);
        collection.add(3);
        collection.add(15);
        collection.add(26);
        collection.add(7);

        // Create an ArbreRougeNoir using the collection
        ArbreRougeNoir<Integer> ArbreRougeNoir = new ArbreRougeNoir<>(collection);

        //System.out.println(ArbreRougeNoir.toString());
        // ArbreRougeNoir<Integer>.Noeud test = ArbreRougeNoir.new Noeud(5);
        // ArbreRougeNoir.supprimer(test);
        // System.out.println(ArbreRougeNoir.racine.minimum().cle.toString());
        Iterator<Integer> iterator = ArbreRougeNoir.new ArbreRougeNoirIterator();

        while (iterator.hasNext()) {
			Integer element = iterator.next();
			//System.out.println(element);
			iterator.remove();
		}
        System.out.println(ArbreRougeNoir.toString());

    }
    @Override
    public Iterator<E> iterator() {
        return new ArbreRougeNoirIterator();
    }

    @Override
    public int size() {
        return taille;
    }
    private class ArbreRougeNoirIterator implements Iterator<E> {
        Noeud courant = racine==sentinelle?sentinelle:racine.minimum();
        Noeud preview = sentinelle;
        public boolean hasNext() {
            return(courant.suivant()!=sentinelle && taille != 0);
        }

        public E next() {
            // TODO
            if(courant == sentinelle){
                throw new NoSuchElementException();
            }
            preview=courant;
            courant=courant.suivant();

            return preview.cle;
        }

        public void remove() {
            // TODO
            if(preview == sentinelle){
                throw new IllegalStateException();
            }
            courant = supprimer(preview);
            preview = sentinelle;
        }
    }

} 