/* Idea */
select
(select email from citiuser.UserProfiles where userid = F.creator) orgin_email,
(select email from citiuser.UserProfiles where userid = F.creator) rcvr_email,
concat("Citi Ideas: Post-",cast(F.nodeid as char)) subj,
concat("Title Description:<p>", F.title, "<p><p>Idea Description: ",
case when F.data is not null then cast(F.data as char)
else (select group_concat(cast(attr_value as char)," ") from citi.IdeaTextAttributesMap where ideaid = F.nodeid)
end) body,
F.title,
from_unixtime(F.modified) timestamp
from citi.FileSystem F
where type = 2
and flags = 1
and modified >= unix_timestamp('@from_time')
and modified < unix_timestamp('@to_time');










