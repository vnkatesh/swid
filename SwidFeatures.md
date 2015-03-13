# Introduction #

The objective is to create a semantic wiki editor for [WikiModel](http://code.google.com/p/wikimodel), supporting text completion, document refactoring, document inline querying, property value completion etc. i.e. a semantic text editor. This semantic editor will be based on the following components: Eclipse RCP text framework, [WikiModel](http://code.google.com/p/wikimodel), an RDF database, a content storage system (local or remote). It will have the features listed below.

# Features #

## Loading of ontologies into an RDF database ##

The user should be able to load ontologies to be used by the editor by entering the URL of the target ontologies.

Example: Load http://xmlns.com/foaf/spec/20071002.rdf and http://www.semanticdesktop.org/ontologies/2007/11/01/pimo/pimo_data.rdfs

The ontologies are then stored as a set of statements in the RDF database.

## Conversion of a wiki page written in [CommonSyntax](http://code.google.com/p/wikimodel/wiki/WikiSyntaxCommon) into a set of statements ##

Example: let's consider the wiki page below "wiki.NEPOMUK\_meeting\_20081112"

```
1 NEPOMUK meeting - November 12 2008

%rdf:type [pimo:Meeting]

%pimo:Attendee [wiki.John_Doe]

%pimo:Attendee [wiki.James_Dunt]

%pimo:hasLocation [wiki.KaisersLautern]
```

The application converts the page above into the following statements:

  * wiki:NEPOMUK\_meeting\_20081112 rdf:type pimo:Meeting
  * wiki:NEPOMUK\_meeting\_20081112 pimo:Attendee wiki:John\_Doe
  * wiki:NEPOMUK\_meeting\_20081112 pimo:hasLocation wiki:KaisersLautern

Other examples (from [WikiModelAdvancedStructuralElements](http://code.google.com/p/wikimodel/wiki/AdvancedStructuralElements)):

```

%title A simple document

%summary A short description of this document. 
It can contain *in-line formatting* and it can be
spawn on multiple lines forming one big paragraph.

%author (((
  %firstName Mikhail
  %lastName Kotelnikov
  %worksIn (((
    %type [Company]
    %name Cognium Systems
    %address (((
         ....
    )))
    %description *Cognium Systems* is a 
    semantic web company...
  )))
)))

```

Inline properties support:

```
I am living in %liveIn(Paris).
```

## Storage of page contents ##

The editor should provide storage capability. Storage can be local or remote. In a first step, page contents will be stored in the filesystem. In a second step, storage calls will be delegated to an external module, executing the operation either locally or remotely. Stored pages can be loaded from the editor. (Note: we should consider reusing here the XWiki-Eclipse storage mechanism: file system XML storage when offline, remote XWiki storage over XML-RPC when online).

## Statement check ##

The application checks the validity of the statements extracted from a page. The validity is checked against the loaded ontologies and existing resources.

Example: in the wiki page above, it checks that "pimo:Attendee" and "pimo:hasLocation" have a range compatible with "pimo:Meeting". It also checks that "wiki:John\_Doe", if existing, has an rdf:type compatible with the domain of "pimo:Attendee", and that "wiki:KaisersLautern", if existing, has an rdf:type compatible with the domain of "pimo:hasLocation". If "wiki:John\_Doe" doesn't exist in the database yet, it is created as a resource having the type "pimo:Person" (same for "wiki:KaisersLautern": created with "rdf:type pimo:Location"). In case inconsistencies are detected, the statements are not stored, and a visual feedback get displayed. If no inconsistency is detected, all the statements are stored (overriding all the previous ones related to the current resource).


## Property completion ##

The editor provides support for completing a property name, based on the page type if specified, and on the selected ontologies.

Example: if the current page has an rdf:type whose value is "pimo:Person", then when the user starts a line with "%", the editor suggests the property names whose range is compatible with "pimo:Person".


## Property value completion ##

The editor provides support for completing a property value based on the domain of the related property.

Example: if the user is editing the value of the property "pimo:attendee", the editor will suggest resource names compatible with the type "pimo:Person".

## Syntax highlighting ##

If possible, after supporting general semantic and [CommonWikiSyntax](http://code.google.com/p/wikimodel), the user-loaded RDF semantic data could also be, possibly highlighted.


## Visual representation of a page properties ##

When a given page is edited, its statements should be presented visually in a dedicated box.

## Inline query ##

The editor lets the user enter SPARQL queries inline and execute them against the database, displaying the results as text in the editor itself.



# Features #

  * ~~syntax highlighting,coloring, content proposal~~
  * ~~real editor for XWiki-Eclipse (check with Fabio)~~
  * ~~integration~~
  * ~~rpc based content proposal~~
  * ~~content outline~~
  * ~~synax conversion using wikimodel~~
  * completion
  * internal refactoring (move a block from one place to another via a menu)
  * cross page refactoring...(use Drag, Drop Handler)
  * intelligent completion: means take into account the properties for completion of property names, and for property values editing (possibly, recognize the current object/context, and use separate completion proposal,partitioner in eclipse text editor)
  * to be completed...

# Further #
  * ~~get to know RDF principles and existing syntaxes: first for inspiration, second for using the existing RDF reasoners like Sesame Reasoner(?) for validating wiki documents on the fly~~

  * ~~automatically, convert all base XWiki classes into WikiModel common syntax documents~~

  * parse a class definition expressed using WikiModel common syntax and convert it into an XWiki class (calling xwiki.createClass(Map)), Status: UI/client side complete, waiting for new xmlrpc api function for createClass(Map<String property,String datatype>,String documentSheet-template), to be incorporated to next XWiki release

  * ~~look into generic type systems, i.e. systems that know how to deal with types, deduce facts from type properties and relations: typically RDF frameworks,~~ but also Groovy type engine.

  * ~~study existing reasoners and choose one for~~ i) constrained contextual completion (with rules, ranges, domains etc.) ii) document validation, Status: Chose RDF2GO, RDFS Reasoner

# Semantic Objectives #
  * ~~get all statements contained in the page~~
  * ~~detect property blocks~~
  * ~~identify property and property values~~
  * ~~create statements in the reasoner model~~
  * check consistencty, Status: Looking for implementation similar to ValidityReport in Jena for RDF2GO
  * provide visual feedback and error description if any

# Todos #

  * release SWID as an Eclipse RCP product
  * look into Vex editor architecture
  * [Java AST manipulation in Eclipse](http://www.eclipse.org/articles/article.php?file=Article-JavaCodeManipulation_AST/index.html)