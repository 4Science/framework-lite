package it.cilea.core.search;

import java.util.LinkedHashSet;
import java.util.Set;

public class HibernateModelAttributeNameWrapper {	
	
	private String attributeName;
	private Set<String> joinSet = new LinkedHashSet<String>();
	
	public HibernateModelAttributeNameWrapper(String fullyQualifiedModelAttribute){
		//Come PREREQUISITO impongo che ogni model attribute sia fully qualified e cioè sia specificato il 
		//percorso dalla radice (oggetto) alla foglia (tipo primitivo)
		//Inoltre per contemplare anche il caso in cui ci sia la necessità di utilizzare un alias diverso 
		//da altri eventualmente già presenti DEVE ESSERE SEGUITA la seguente naming convention:
		
		//Effettuo lo split sul punto e ottengo tutti i nomi degli attributi figli 
		//fino ad arrivare al tipo primitivo da usare all'interno della query
		String[] piece=fullyQualifiedModelAttribute.split("\\.");		
		//Dato che il fullyQualifiedModelAttribute DEVE essere fully qualified, conterrà almeno due pezzi separati
		//da ".": il primo è l'entità radice mentre il secondo è l'attributo primitivo.

		//Nel caso in cui fullyQualifiedModelAttribute contenga più di 2 elementi, devo  
		//costruire i join
		
		for (int i=0;i<piece.length-1;i++){
			ModelAttributeJoinWrapper parentObject = new ModelAttributeJoinWrapper(piece[i]);
			ModelAttributeJoinWrapper childObject = new ModelAttributeJoinWrapper(piece[i+1]);
			//se l'indice è = a piece.length-2 allora vuol dire che sono arrivato all'ultimo 
			//livello e quindi non devo più creare left join ma solo l'attributo effettivo di query hql
			if (i!=piece.length-2){
				String joinClause=childObject.getJoinType() + parentObject.getAttributeAlias() + "." + childObject.getAttributeName() + " " + childObject.getAttributeAlias();
				joinSet.add(joinClause);
			} else {					
				attributeName = parentObject.getAttributeAlias()+"."+childObject.getAttributeName();
			}
		}
	}
	
	//Questa classe permette di gestire dato un singolo pezzo del fully qualified name
	//di recuperare il nome dell'attributo, l'eventuale alias da usare nel join e anche il tipo 
	//di join da effettuare
	
	//di seguito un pattern di esempio:
	//radice.+attr1Set.attr2Set_attr2Alias.attr3
	//Il punto serve per separare gli attributi
	//L'underscore (_) per inserire l'opzionale alias da utilizzare nei join
	//Il segno più (+) serve per utilizzare un inner join al posto del left join utilizzato
	//di default
	//Guardare anche il metodo statico presente in coda a questa classe
	class ModelAttributeJoinWrapper{
		private String attributeName;
		private String attributeAlias;
		private String joinType;
		
		public ModelAttributeJoinWrapper(String value){
			
			if (value.startsWith("+")){
				joinType=" inner join ";
				value=value.replace("+", "");
			}
			else
				joinType=" left join ";			
			
			String[] piece=value.split("_");
			attributeName=piece[0];
			if (piece.length>1)
				attributeAlias=piece[1];
			else
				attributeAlias=attributeName;
			
		}
		public String getAttributeName() {
			return attributeName;
		}
		public String getAttributeAlias() {
			return attributeAlias;
		}
		
		public String getJoinType() {
			return joinType;
		}
	}
	
	public Set<String> getJoinSet(){		
		return joinSet;
	}

	public String getAttributeName() {
		return attributeName;
	}
	
	public static void main(String[] args) {
		//String str="scheda.membroSet.persona.cognome";
		//String str="scheda.membroSet.persona.indirizzoSet_ancora.indirizzo";
		//String str="scheda.membroSet_partecipante.discriminator";
		String str="scheda.+membroSet_partecipante.persona_ppp.cognome";
		HibernateModelAttributeNameWrapper t = new HibernateModelAttributeNameWrapper(str);
		Set<String> joinSet=t.getJoinSet();
	}

}
