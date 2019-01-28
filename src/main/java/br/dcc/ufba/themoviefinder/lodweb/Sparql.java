/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.dcc.ufba.themoviefinder.lodweb;

import java.util.Set;

public class Sparql {

	public static char quotes = '"';

	public static String wrapStringAsResource(String value)
	{
		if(value != null) {
			value = value.trim().replaceAll("\\s", "_").replaceAll("\\_+", "_");
            if(value.charAt(value.length() - 1) == '_') value = value.substring(0, value.length() - 1);
            value = value.replaceAll("\\?+", "").trim();
            if(value.length() > 1) {
            	return new StringBuilder("http://dbpedia.org/resource/").append(value).toString();
            }
		} else {
			throw new NullPointerException("value must not be null");
		}
		return null;
	}


	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringUnionDirectInBothWaysForCheckingCount(String uri1, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String stringUri : uris) {
			stringBuilder.append(" " + " UNION " + " { <" + uri1.trim() + "> ?p1  <" + stringUri.trim() + "> } "
					+ "  UNION " + " { <" + stringUri.trim() + "> ?p1  <" + uri1.trim() + "> } ");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionIndirectIncoming(String uri1, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String stringUri : uris) {
			stringBuilder.append(" " + " UNION " + " { values (?r1 ?r3){(<" + uri1.trim() + "> <" + stringUri.trim()
					+ ">)}  ?r1 ?p1 ?r2 .  ?r3 ?p1 ?r2 } ");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionIndirectOutgoing(String uri1, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String stringUri : uris) {
			stringBuilder.append(" " + " UNION " + " { values (?r1 ?r3){(<" + uri1.trim() + "> <" + stringUri.trim()
					+ ">)}  ?r2 ?p1 ?r1 .  ?r2 ?p1 ?r3 } ");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionDirectOneWay(String uri1, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String stringUri : uris) {
			stringBuilder.append(" " + " UNION " + " { values (?r1 ?r2){(<" + uri1.trim() + "> <" + stringUri.trim()
					+ ">)}  ?r1 ?p1 ?r2 . } ");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionDirectBothWays(String uri1, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();

		for (String stringUri : uris) {

			stringBuilder.append(" " + " UNION " + " { values (?r1 ?r2){(<" + uri1.trim() + "> <" + stringUri.trim()
					+ ">)}  ?r1 ?p1 ?r2  } UNION { values (?r1 ?r2){(<" + uri1.trim() + "> <" + stringUri.trim()
					+ ">)}  ?r2 ?p1 ?r1  }");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}


	//fred
	public static String getStringQueryValuesUnionDirectBothWaysForNodes(String uri1, Set<Node> nodes)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (Node node : nodes) {
			stringBuilder.append(" " + " UNION " + " { values (?r1 ?r2){(<" + uri1 + "> <" + node.getURI()+ ">)}  ?r1 ?p1 ?r2} "
					+ "UNION { values (?r1 ?r2){(<" + uri1 + "> <" + node.getURI()+ ">)}  ?r2 ?p1 ?r1  }");
		}
		String ss = stringBuilder.toString().replaceFirst("UNION", "");
		// Lodica.print(ss);
		return ss;
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionIncomingAndOutgoing(String uri1, Set<String> uris)
	{
		String incoming = getStringQueryValuesUnionIndirectIncoming(uri1, uris);
		String outgoing = getStringQueryValuesUnionIndirectIncoming(uri1, uris);
		StringBuilder finalString = new StringBuilder();
		if (!incoming.trim().isEmpty() && !outgoing.trim().isEmpty()) {
			finalString.append(incoming);
			finalString.append(" UNION ");
			finalString.append(outgoing);
		} else if (!incoming.trim().isEmpty() && outgoing.trim().isEmpty()) {
			finalString.append(incoming);
		} else {
			finalString.append(outgoing);
		}
		return finalString.toString();
	}

	/**
	 * @param uri1
	 * @param uris
	 * @return
	 */
	public static String addFilterURIs(String var, Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (String stringUri : uris) {
			stringBuilder.append(" . " + " FILTER(STRSTARTS(STR(?" + var + "), <" + stringUri + ">))  ");
		}
		String ss = stringBuilder.toString().replaceFirst(".", "");
		// Lodica.print(ss);
		return ss;
	}

	public static String addPrefix()
	{
		StringBuilder prefix = new StringBuilder();
		prefix.append("");
		prefix.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
		prefix.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
		prefix.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		prefix.append("PREFIX dbo: <http://dbpedia.org/ontology/> ");
		prefix.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
		prefix.append("PREFIX foaf: <http://xmlns.com/foaf/0.1/> ");
		prefix.append("PREFIX dct: <http://www.w3.org/2000/01/rdf-schema#> ");
		prefix.append("PREFIX dc: <http://purl.org/dc/elements/1.1/> ");
		prefix.append("PREFIX bif: <bif:> ");
		prefix.append("PREFIX : <http://dbpedia.org/resource/> ");
		prefix.append("PREFIX dbpedia2: <http://dbpedia.org/property/> ");
		prefix.append("PREFIX dbpedia: <http://dbpedia.org/> ");
		prefix.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ");
		prefix.append("PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> ");
		prefix.append("PREFIX e: <http://learningsparql.com/ns/expenses#> ");
		prefix.append("PREFIX d: <http://learningsparql.com/ns/data#> ");
		return prefix.toString();

	}


	public static String addLangFilter(String subject, String property, String object)
	{
		String subject1 = " ";
		String property2 = " ";
		String object3 = " ";
		if (subject != null) { }

		if (property != null) {
			property2 = " FILTER(?" + property + " != <http://dbpedia.org/ontology/wikiPageID>)." + "FILTER(?"
					+ property + " != <http://dbpedia.org/ontology/wikiPageRevisionID>). ";
			// property2 = " " ;
		}

		if (object != null) {
			object3 = " FILTER(lang(?" + object + ") = " + quotes + "" + quotes + " || langMatches(lang(?" + object
					+ ")," + quotes + "en" + quotes + "))    . ";
		}
		String filter = subject1.concat(property2).concat(object3).trim();
		return filter;
	}

	public static String addService(boolean usingGraph, String serviceURI)
	{
		if (!usingGraph) {
			return "SELECT DISTINCT * WHERE { SERVICE <" + serviceURI + "> {";
		} else {
			return " ";
		}
	}

	public static String addServiceClosing(boolean usingGraph)
	{
		if (!usingGraph) {
			return "}}";
		} else {
			return " ";
		}
	}

	public static String checkLimitNull(Object object)
	{
		if (object == null) {
			return " ";
		} else {
			return "LIMIT " + object.toString();
		}
	}

	/**
	 * @return
	 */
	public static String addFilterDomain2(String subject, String object)
	{
		String subject1 = " ";
		String object3 = " ";

		if (subject != null) {
			subject1 = "((?" + subject + " a <http://dbpedia.org/ontology/Film> ) OR " + "(?" + subject
					+ " a <http://dbpedia.org/ontology/WrittenWork> ) OR " + "(?" + subject
					+ " a <http://dbpedia.org/ontology/Organisation> )) . ";

		}

		if (object != null) {
			object3 = "((?" + object + " a <http://dbpedia.org/ontology/Film> ) OR " + "(?" + object
					+ " a <http://dbpedia.org/ontology/WrittenWork> ) OR " + "(?" + object
					+ " a <http://dbpedia.org/ontology/Organisation> )) . ";

		}

		String filter = subject1.concat(object3).trim();

		return filter;

	}

	/**
	 * @return
	 */
	public static String addFilterDomain(String subject, String object)
	{
		String subject1 = " ";
		String object3 = " ";

		if (subject != null) {
			subject1 = "{?" + subject + " a ?type }. FILTER( ?type IN(" + " <http://dbpedia.org/ontology/Film>,"
					+ " <http://dbpedia.org/ontology/WrittenWork>," + " <http://dbpedia.org/ontology/Organisation>))";
		}

		if (object != null) {
			object3 = "{?" + object + " a ?type } . FILTER( ?type IN(" + " <http://dbpedia.org/ontology/Film>,"
					+ " <http://dbpedia.org/ontology/WrittenWork>," + " <http://dbpedia.org/ontology/Organisation>))";
		}
		String filter = subject1.concat(object3).trim();
		return filter;

	}

	/**
	 * @author Gabriela
	 * @param uris
	 * @return
	 */
	public static String getStringQueryValuesUnionDirectOneWay(Set<String> uris)
	{
		StringBuilder stringBuilder = new StringBuilder();

		for (String stringUri : uris) {

			stringBuilder.append( " "
					+ " UNION "
					+ " { values (?r1){( <" + stringUri.trim()+">)}  ?r1 ?p1 ?r2 . } " );
		}
		String ss = stringBuilder.toString().replaceFirst("UNION","");
		//Lodica.print(ss);

		return ss;
	}


	/**
	 * @return
	 */
	public static String addFilter(String property, String object)
	{
		String property2 = "";
		String object2 = "";
		if (property != null) {
			property2 += " FILTER(?"+ property + " != <http://dbpedia.org/ontology/wikiPageID>) . FILTER(?"+ property + " != <http://dbpedia.org/ontology/wikiPageRevisionID>) . FILTER(?"+ property + " != <http://dbpedia.org/ontology/wikiPageRedirects>) . FILTER(?"+ property + " != <http://dbpedia.org/ontology/wikiPageExternalLink>)";
		}
		if (object != null) {
			object2 += " FILTER(!isLiteral(?" + object + "))";
		}
		if(! object2.isEmpty()) {
			return property2 + " . " + object2;
		}
		return property2 + object2;
	}
}