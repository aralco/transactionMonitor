/* Email History */
select  
sender orgin_email,
receiver rcvr_email,
concat("Citi Ideas: Email ", cast(F.emailhistoryid as char)) subj,
content body,
subject title,
from_unixtime(F.datesent) timestamp
from EmailHistory F
where datesent >= unix_timestamp('@from_time')
and datesent < unix_timestamp('@to_time')
and sender not like "%root@spigit.com%"
and sender not like "%auto@spigit.com%"
and sender not like "%notifier@spigit.com%"
and subject not like "Check out this idea:%";

