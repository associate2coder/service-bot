# Telegram bot

## Purpose

Development of the telegram bot to assist with the organization and management of deliveries.
In short, the bot is aimed to provide employees with the means to retrieve information needed for their work without disturbing the management (or sharing access/passwords or restricted systems).
So far, the bot saved me more time (around 10-15 minutes a day, 5-6 days per week, for more than a year) than I spent for its development.

This code is a work-in-progress product. Not to be used in production.

## Features

- Retrieving (via Nova Poshta API) of waybill registers (scan sheets). Transformation of the register's number into the barcode which can be scanned at self-service desk in a Nova Poshta post office;
- Checking (per request of a user) whether packages are not picked up by customers (and free storage time has expired or is to expire on the next day).
- Bot has functionality accesible to users and admin section (access control, managing Nova Poshta API keys);
- Access control is made via registering by admins of user's telegram user id numbers (chat id). Users request access (by doing so, they share their user id number) and provide their telegram contact infor (including phone number) for verification;
- Admins can add Nova Poshta API keys (as business make deliveries from several people, or in case existing API key has expired);
- Telegram user id number (chat id) of a super user (me) is provided as environmental variable and is written to db at application start.

## Future enhancements

- minor additions (editing and deleting users, editing and deleting expired API keys);
- automatic checking for paid storage of packages according to the user rule (user scheduled task);
- request returning of packages refused by customers via functionality of the bot.

## Contact Information
- [@LeksUkr](https://t.me/LeksUkr)https://t.me/LeksUkr
