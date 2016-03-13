SELECT *
FROM meishi
  NATURAL JOIN company
WHERE login_user = /*loginUser*/'' AND company_id = /*companyId*/1
/*# orderBy */;