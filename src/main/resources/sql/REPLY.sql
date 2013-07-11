/* Reply */
select  
(select email from citiuser.UserProfiles where userid = F.creator) orgin_email,
(select email from citiuser.UserProfiles where userid = (select creator from FileSystem where nodeid = F.parentid)) rcvr_email,
concat("Citi Ideas: Post-",(select cast(parentid as char) from FileSystem where nodeid = F.parentid)," ParentComment-",cast(F.parentid as char)," Comment-",cast(F.nodeid as char)) subj,
concat("Comment Description: ",cast(F.data as char)) body,
F.title,
from_unixtime(F.modified) timestamp
from FileSystem F
where type = 18
and flags = 1
and modified >= unix_timestamp('@from_time')
and modified < unix_timestamp('@to_time');
