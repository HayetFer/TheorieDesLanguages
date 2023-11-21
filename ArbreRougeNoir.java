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
			gauche = null;
			droit = null;
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
			while (actuel.gauche != null) {
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
			if (droit != null) {
				return droit.minimum(); // Si le nœud a un sous-arbre droit, le successeur sera le nœud avec la clé
										// minimale de ce sous-arbre droit.
			} else {
				Noeud actuel = this;
				Noeud pereActuel = pere;

				// Tant que le nœud est le fils droit de son parent, on remonte dans l'arbre
				while (pereActuel != null && actuel == pereActuel.droit) {
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
		racine = null;
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
		racine = null;
		this.taille = 0;
	}
	
	public ArbreRougeNoir(Collection<? extends E> c) {
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
		return true;
	}
	

	public void ajouterCorrection(Noeud z) {
		while (z.pere.couleur == Couleur.Rouge) {
			if (z.pere == z.pere.pere.gauche) {
				Noeud y = z.pere.pere.droit; // Uncle of z
				if (y.couleur == Couleur.Rouge) {
					// Case 1
					z.pere.couleur = Couleur.Noir;
					y.couleur = Couleur.Noir;
					z.pere.pere.couleur = Couleur.Rouge;
					z = z.pere.pere;
				} else {
					if (z == z.pere.droit) {
						// Case 2
						z = z.pere;
						rotationGauche(z);
					}
					// Case 3
					z.pere.couleur = Couleur.Noir;
					z.pere.pere.couleur = Couleur.Rouge;
					rotationDroite(z.pere.pere);
				}
			} else {
				// Mirror image of the above cases for the right side
				Noeud y = z.pere.pere.gauche; // Uncle of z for the right side
				if (y.couleur == Couleur.Rouge) {
					// Case 1'
					z.pere.couleur = Couleur.Noir;
					y.couleur = Couleur.Noir;
					z.pere.pere.couleur = Couleur.Rouge;
					z = z.pere.pere;
				} else {
					if (z == z.pere.gauche) {
						// Case 2'
						z = z.pere;
						rotationDroite(z);
					}
					// Case 3'
					z.pere.couleur = Couleur.Noir;
					z.pere.pere.couleur = Couleur.Rouge;
					rotationGauche(z.pere.pere);
				}
			}
		}
		// Adjust root color after corrections
		racine.couleur = Couleur.Noir;
	}
	
	private void rotationDroite(Noeud x) {
        Noeud y = x.gauche;
        x.gauche = y.droit;
        if (y.droit != null) {
            y.droit.pere = x;
        }
        y.pere = x.pere;
        if (x.pere == null) {
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
        if (y.gauche != null) {
            y.gauche.pere = x;
        }
        y.pere = x.pere;
        if (x.pere == null) {
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
		System.out.println(newNode.cle.toString());
		ajout(newNode);
		return true;
	}

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
		return x == null ? 0
				: Math.max(x.cle.toString().length(),
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
		//collection.add(5);
		collection.add(7);

		// Create an ArbreRougeNoir using the collection
		ArbreRougeNoir<Integer> ArbreRougeNoir = new ArbreRougeNoir<>(collection);

		System.out.println(ArbreRougeNoir.toString());
		// ArbreRougeNoir<Integer>.Noeud test = ArbreRougeNoir.new Noeud(5);
		// ArbreRougeNoir.supprimer(test);
		// System.out.println(ArbreRougeNoir.racine.minimum().cle.toString());
		// Iterator<Integer> iterator = ArbreRougeNoir.new ArbreRougeNoirIterator();
		// // Use the iterator to iterate through the elements
		// iterator.next();
		// iterator.remove();

		// while (iterator.hasNext()) {
		// Integer element = iterator.next();
		// System.out.println(element);
		// }

	}

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
			
			// if(preview == null){
			// 	throw new IllegalStateException();
			// }
			// courant=supprimer(preview);
			// preview=null;

		}
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
		public boolean hasNext() {
			// TODO
			return false;
		}

		public E next() {
			// TODO
			return null;
		}

		public void remove() {
			// TODO
		}
	}

}

