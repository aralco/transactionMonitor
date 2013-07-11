/* Tell a Friend */
select  
(select email from citiuser.UserProfiles where userid = F.userid) orgin_email,
F.email rcvr_email,
concat("Citi Ideas: Tell-A-Friend ", cast(F.emailfriendhistoryid as char)) subj,
cast(F.data as char) body,
concat((select title from FileSystem where nodeid = ideaid)," Tell-A-Friend") title,
from_unixtime(F.datesent) timestamp
from EmailFriendHistory F
where datesent >= unix_timestamp('@from_time')
and datesent < unix_timestamp('@to_time');

