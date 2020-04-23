Task: Synchronizing a unisex toilet
-   At one university, there's a toilet for both genders which both men and women can use, but under these rules:
    -   If there's a woman in the toilet, access is allowed only for other women;
    -   If there's a man in the toilet, access is allowed only for other men;
    
-   There is a sign at the toilet entrance which indicates the state: empty, women inside, men inside.
-   Write the following methods for the scenarios:
    -   woman_enters();
    -   woman_exits();
    -   man_enters();
    -   man_exits();
    
-   You can use counters and techniques as you wish.
-   When a person enters the toilet it should call wc.enter(), and wc.exit() when it exits.
    -   The wc variable is already defined.
    -   The enter() and exit() methods are not atomic, and you should take care of their synchronization.

-   During execution, there are multiple male and female threads working in parallel, trying to access the toilet.