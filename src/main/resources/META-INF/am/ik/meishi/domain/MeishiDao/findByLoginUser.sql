SELECT *
FROM meishi
  NATURAL JOIN company
WHERE login_user = /*loginUser*/''
/*# orderBy */;