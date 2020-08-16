# Evidence studentu

Semestralni prace pro predmet BI-EJA, je napsana pomoci Spring Boot.

## Popis:

Je popsano 3 entitnich tridy: Student, Faculty, Course. 

Vztahy mezi tabulkami v db(MySQL):

* Student-Course(Many to Many)
* Student-Faculty(Many to One)
* Faculty-Course(One to Many)


Pro view je pouzita technologie JSP. 
Pro komunikaci s API je pouzita technologie REST.

## Spusteni:

V tride StudentEvidenceApplication spustit metodu run().
