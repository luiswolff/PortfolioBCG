# PortfolioBCG

Dies ist ein Datenanalyse-Tool, welches Daten nach dem Marktwachstum-Marktanteil-Portfolio der Boston Consulting Group aufbereitet. Dieses Portfolio betrachtet ein Unternehmen und seine sogenannten strategischen Gesch&auml;ftseinheiten (engl.: strategic business unit - SBU). Die X-Position gibt Auskunft dar&uuml;ber, wie stark dieses gegen&uuml;ber den st&auml;rksten Mitbewerber ist. Die Y-Achse gibt Auskunft dar&uuml;ber, wie stark der jeweilige Markt w&auml;chst.

Das Tool wurde in Rahmen einer Semesterarbeit an der Fachhochschule Stralsund geschrieben. Es ist in der Sprache Java geschrieben und baut auf der Processing-Core-Engine auf.

## Elemente

Das Tool besteht aus mehreren Elementen, welche die Oberfl&auml;che &uuml;ber die Methode `draw()` von `Portfolio` im Paket `de.wolff.portfolioBCG` erzeugen. Die Steuerungselemente befinden sich dabei im Paket `de.wolff.portfolioBCG.elements`. Es wird folgende Funktionalit&auml;t erzeugt:

1. Es ist m&ouml;glich, die Daten in Perioden zu unterteilen und diese mit Hilfe des Elements `Scrollbar`, welches unter dem Portfolio angezeigt wird, zu w&auml;hlen. Dabei wechselt die Periode erst, wenn Slider eine der angezeigten Markierungen &uuml;berschreitet.
2. Das Element `CheckBox`, was oben rechts angezeigt wird, gibt an, ob die Werte der Vorperioden der SBUs angezeigt werden sollen. (__Achtung:__ Dadurch kann das Portfolio sehr un&uuml;bersichtlich werden, wenn es viele SBUs gibt.)
3. Es gibt noch ein Element `SelectOneButtons` was allerdings zurzeit keine Verwendung hat. Es wurde zu einem fr&uuml;herem Zeitpunkt entwickelt, wo der Entwurf noch etwas anders aussah. Dieser wurde aber ge&auml;ndert, wodurch dieses Element zurzeit noch &uuml;berfl&uuml;ssig ist.

Elemente, welche die Datenaufbereitung selbst betreffen wurden als Innere-Klassen erzeugt. Hierbei wurden folgende Elemente erzeugt:

1. `PortfolioArea`: Der Bereich, welcher das Vier-Felder-Portfolio darstellt. Es benutzt die Elemente `Abscissa` und `Ordinate` aus dem Paket `de.wolff.portfolioBCG.elements` um zu erreichen, welche Position ein SBU bekommt. Der Radius eines Kreises h&auml;ngt von Anteil der SBU zum Gesamtumsatz ab.
2. `PortfolioInfo`: Dieser Bereich zeigt an, welche SBU f&uuml;r die jeweilige Periode relevant sind. Gesch&auml;ftseinheiten, auf denen das betrachtete Unternehmen nicht aktiv ist k&ouml;nnen andersfarbig dargestellt werden. Ferner kann per Maus-Klick ein Kreis im Portfolio ausgew&auml;hlt werden, wodurch nun angezeigt wird, mit welchen Marken das Unternehmen und sein st&auml;rkster Rivale agieren. 

## Einstellungen

Es ist m&ouml;glich unterschiedliche Einstellungen f&uuml;r das Tool vorzunehmen. Dazu m&uuml;ssen lediglich die Eintr&auml;ge der `*.properties`-Dateien im Ordner `conf/` ge&auml;ndert werden. Diese werden mit einer Instanz von `Settings` im Pakte `de.wolff.portfolioBCG` eingelesen. Dies wird mit Hilfe eines Objekts von Typ `java.util.Properties` gemacht. Die Eintr&auml;ge werden nun wahlweise als `String`, `int`, `float` oder `de.wolff.portfolioBCG.Settings$Color`. 

## Run

Das Tool PortfolioBCG ist an sich kein lauff&auml;higes Programm. Um es zu starten muss zun&auml;chst das Interface `MarketData` aus dem Paket `de.wolff.portfolioBCG` implementiert werden. Danach muss an der Klasse `Portfolio` im gleichen Paket die Methode `analyseData(String dataClass, String[] args)` aufgerufen werden. Die Parameter haben dabei folgende Bedeutung:

* `dataClass`: Dies ist der vollst&auml;ndige Klassen-Name der Implementierung von `MarketData`. 
* `args`: Array von Argumenten, die &uuml;ber die Java-Konsole eingegeben wurden. Diese werden von Processing weiter verarbeitet.

Im Paket `de.wolff.portfolioBCG.testRun` sind die Klassen `Launcher` und `TestData` die zeigen, wie dies umgesetzt werden kann. Es handelt sich hierbei allerdings nur um Testdaten, die hart kodiert sind. Sie wurden f&uuml;r das Debugging w&auml;hrend der Entwicklung benutzt. Eine andere Umsetzung kann im Reposotory [PortfolioRegistrationsGermany] (https://github.com/luiswolff/PortfolioRegistrationsGermany "Portfolio fuer Autoneuzulassungen in Deutschland") betrachtet werden.

## Modells

Das Tool arbeitet mit Modells, die benutzt werden, um die zu analysierenden Daten im Hauptspeicher zu verwalten. Alle befinden sich im Paket `de.wolff.portfolioBCG.modells`. Dieses Tool selbst liest dabei deren Datenfelder nur aus. Ihre Erzeugung ist der Implementierung vom Interface `MarketData` &uuml;berlassen. Die Modells besitzen dabei folgende Struktur:

* `Manufacture`: Stellen Akteure auf den zu betrachtenden M&auml;rkten dar. Hierunter fallen das zu betrachtende Unternehmen und deren Konkurrenten. Hier wird lediglich ein Name gespeichert, welche zu anzeige verwendet wird.

* `Period`: Stellt eine zu betrachtende Periode dar. Sie speichert dabei...:

	* ... ihren Name, welcher zur Anzeige benutzt wird, ... 
	* ... den gesamten Umsatz welcher durch das zu betrachtende Unternehmen in ihr erwirtschaftet wurde und...
	* ... eine Liste der f&uuml;r ihr relevanten strategischen Gesch&auml;ftseinheiten.
	
* `PeriodSBU`: Eine strategische Gesch&auml;ftseinheit f&uuml;r eine Periode. Sie speichern...:

	* ... ihren Marktwachstum f&uuml;r die Periode, ...
	* ... ihren Namen, ...
	* ... eine Liste der Marken vom zu betrachtendem Unternehmen sowie deren Marktanteil, ...
	* ... eine Liste von Marken eines jeden Konkurrenten und ... 
	* ... den st&auml;rk Konkurrenten sowie dessen Marken und Marktanteil.
	
	Der st&auml;rkste Konkurrent wird dabei &uuml;ber die Methode `addBrand(PeriodBrand)` ermittelt. Dabei wird die Markenliste des Herstellers aktualisiert und gepr&uuml;ft, ob er nun st&auml;rker ist, als der bereits bekannte.

* `PeriodBrand`: Eine Marke, die innerhalb einer Periode vertrieben wurde. Sie speichert...:
	
	* ... welchen Hersteller sie hat, ...
	* ... wie sie hei&szlig;t und...
	* ... welchen Umsatz sie generiert hat.
	
## Literatur und Quellen

Das Programm wurde mit Hilfe folgender Literatur erstellt:

Autor|Titel|Jahr|Ort|Verleger|Auflage|
---|---|---|---|---|---|
Kotler, Philip; Keller, Kevin Lane; Friedhelm Bliemel|Marketing-Management - Strategien f&uuml;r wertschaffendes Handeln|2007|Hallbergmoos|Pearson Studium|12|
Haedrich, G&uuml;nther; Tomczak, Torsten|Produktpolitik|1996|Stuttgart|Kohlhammer||
Becker, Jochen|Marketing-Konzeption|2013|M&uuml;nchen|Verlag Franz Vahlen M&uuml;nchen|10|

Nat&uuml;rlich flo&szlig;en auch Einfl&uuml;sse aus der Marketing-Vorlesung, des Studiengangs Wirtschaftsinformatik im Sommersemester 2014, ein.

Ferner wurde Quellcode von folgenden Seiten verwendet:

* http://processing.org/examples/scrollbar.html
* http://www.processing.org/examples/button.html
