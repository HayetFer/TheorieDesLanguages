import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
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
public class ABR<E> extends AbstractCollection<E> {
	private Noeud racine;
	private int taille;
	private Comparator<? super E> cmp;

	private class Noeud {
		E cle;
		Noeud gauche;
		Noeud droit;
		Noeud pere;

		Noeud(E cle) {
			this.cle=cle;
            gauche=null;
            droit=null;
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
			while(actuel.gauche!=null){
                actuel=actuel.gauche;
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
            if (droit != null) 
            {
                return droit.minimum(); // Si le nœud a un sous-arbre droit, le successeur sera le nœud avec la clé minimale de ce sous-arbre droit.
            } 
            else {
                Noeud actuel = this;
                Noeud pereActuel = pere;
        
                // Tant que le nœud est le fils droit de son parent, on remonte dans l'arbre
                while (pereActuel != null && actuel == pereActuel.droit) {
                    actuel = pereActuel;
                    pereActuel = pereActuel.pere;
                }
        
                // Si le nœud n'est pas le fils droit de son parent, alors le parent est le successeur
                return pereActuel;
            }
		}
	}

	// Consructeurs

	/**
	 * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
	 */
	public ABR() {
		racine = null ;
        this.taille=0;
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
	 * le comparateur
	 * 
	 * @param cmp
	 *            le comparateur utilisé pour définir l'ordre des éléments
	 */
	public ABR(Comparator<? super E> cmp) {
		cmp = (e1,e2)->((Comparable<E>)e1).compareTo(e2);
        racine=null;
        this.taille=0;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 * 
	 * @param c
	 *            la collection à copier
	 */ 
	public ABR(Collection<? extends E> c) {
        Iterator<? extends E> iterator = c.iterator();
		while(iterator.hasNext()){
            this.add(iterator.next());
        }
	}
    public boolean add(E elt) {
        if (racine == null) {
            racine = new Noeud(elt);
            taille = 1;
            return true;
        }
		cmp = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
        Noeud actuel = racine;
        int res;
        while (actuel.gauche != null || actuel.droit != null) {
            res = cmp.compare(elt, actuel.cle);   
            if (res == 0) {
                return false;
            }
    
            if (res < 0) {
                if (actuel.gauche == null) {
                    actuel.gauche = new Noeud(elt);
                    actuel.gauche.pere=actuel;
                    return true;
                }
                actuel = actuel.gauche;
            } else {
                if (actuel.droit == null) {
                    actuel.droit = new Noeud(elt);
                    actuel.droit.pere=actuel;
                    return true;
                }
                actuel = actuel.droit;
            }
        }
        res = cmp.compare(elt, actuel.cle);
         if (res < 0) {
                if (actuel.gauche == null) {
                    actuel.gauche = new Noeud(elt);
					actuel.gauche.pere=actuel;
              }
        }
		else if((res > 0)) {
                if (actuel.droit == null) {
                    actuel.droit = new Noeud(elt);
					actuel.droit.pere=actuel;
                    return true;
                }
            }

    
        return true;
    }
	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	@Override
	public int size() {
		return taille;
	}

	// Quelques méthodes utiles

	/**
	 * Recherche une clé. Cette méthode peut être utilisée par
	 * {@link #contains(Object)} et {@link #remove(Object)}
	 * 
	 * @param o
	 *            la clé à chercher
	 * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
	 */
	private Noeud rechercher(Object o) {
        cmp = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
        try{
            if(racine!=null){
            Noeud actuel = racine;
            while(actuel!=null){
                int res = cmp.compare((E) o, actuel.cle);
                if(res==0){
                    return actuel;
                    }
                if(res<0){
                    if(actuel.gauche==null){
                        return null;
                    }
                    actuel=actuel.gauche;
                    }
                else{
                    if(actuel.droit==null){
                        return null;
                    }
                    actuel=actuel.droit;
                }
                }
        }
        }
        catch(NullPointerException e){
            return null;
        }
		
		return null;
	}

	/**
	 * Supprime le noeud z. Cette méthode peut être utilisée dans
	 * {@link #remove(Object)} et {@link Iterator#remove()}
	 * 
	 * @param z
	 *            le noeud à supprimer
	 * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
	 *         clés. Cette valeur de retour peut être utile dans
	 *         {@link Iterator#remove()}
	 */
	private Noeud supprimer(Noeud z) {
        z = rechercher(z.cle);
		if(z==racine){
			if(z.gauche==null && z.droit == null){
				Noeud Temp=racine.suivant();
				racine = null;
				return Temp;
        	}
			else if(z.gauche==null && z.droit != null){
				z.droit.pere=z.droit;
				racine=z.droit;
			}
        	else if(z.gauche!=null && z.droit == null){
				z.gauche.pere=z.gauche;
				racine=z.gauche;
			}
		}
		if(z.gauche==null && z.droit == null){
            Noeud Temp = z.pere;
			boolean compareResult = cmp.compare(Temp.cle, z.cle) < 0;
			if (compareResult) {
				z.pere.droit=null;
			} else {
				Temp.gauche = null;
			}     
        }
        if(z.gauche==null && z.droit != null){
			Noeud Temp = z.pere;
			boolean compareResult = cmp.compare(Temp.cle, z.cle) < 0;

			if (compareResult) {
				Temp.droit = z.droit;
			} else {
				Temp.gauche = z.droit;
			}            }
        if(z.gauche!=null && z.droit == null){
            Noeud Temp = z.pere;
			boolean compareResult = cmp.compare(Temp.cle, z.cle) < 0;

			if (compareResult) {
				Temp.droit = z.gauche;
			} else {
				Temp.gauche = z.gauche;
			}      
        }
        else if(z.gauche!=null && z.droit != null){
            Noeud min = z.suivant();
			E cle = z.suivant().cle;
			supprimer(min);
			z.cle=cle;
        }
		return z.suivant();
	}

	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ABRIterator implements Iterator<E> {
		Noeud courant = racine==null?null:racine.minimum();
		Noeud preview = null;
		public boolean hasNext() {
			return(courant.suivant()!=null && taille != 0);
		}

		public E next() {
			if(courant == null){
				throw new NoSuchElementException();
			}
			preview=courant;
			courant=courant.suivant();

			return preview.cle;
		}

		public void remove() {
			
			if(preview == null){
				throw new IllegalStateException();
			}
			courant=supprimer(preview);
			preview=null;

		}
	}

	// Pour un "joli" affichage

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(racine, buf, "", maxStrLen(racine));
		return buf.toString();
	}

	private void toString(Noeud x, StringBuffer buf, String path, int len) {
		if (x == null)
			return;
		toString(x.droit, buf, path + "D", len);
		for (int i = 0; i < path.length(); i++) {
			for (int j = 0; j < len + 6; j++)
				buf.append(' ');
			char c = ' ';
			if (i == path.length() - 1)
				c = '+';
			else if (path.charAt(i) != path.charAt(i + 1))
				c = '|';
			buf.append(c);
		}
		buf.append("-- " + x.cle.toString());
		if (x.gauche != null || x.droit != null) {
			buf.append(" --");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len);
	}

	private int maxStrLen(Noeud x) {
		return x == null ? 0 : Math.max(x.cle.toString().length(),
				Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
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
		collection.add(5);
		collection.add(7);

        // Create an ABR using the collection
        ABR<Integer> abr = new ABR<>(collection);

        System.out.println(abr.toString());
        // ABR<Integer>.Noeud test = abr.new Noeud(5);
        // abr.supprimer(test);
       // System.out.println(abr.racine.minimum().cle.toString());
		Iterator<Integer> iterator = abr.new ABRIterator();
		// Use the iterator to iterate through the elements
			iterator.next();
			iterator.remove();
		
		
		while (iterator.hasNext()) {
			Integer element = iterator.next();
			System.out.println(element);
		}
		
    }
}
