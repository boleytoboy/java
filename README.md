﻿# OpendotaAPI
OpendotaAPI is a Java Maven server that accepts player's ID or match ID as input and returns a JSON object containing information about player or match.
## Technologies Used:
1. Java
2. Spring Boot
3. Maven

### Example
```
http://localhost:8080/info?id=887997038
```
```json
{
  "profile":{"account_id":887997038, 
  "personaname":"болею тобой",
  "name":null,
  "plus":false,
  "profileurl":"https://steamcommunity.com/id/ilovekazahov/",
  "steamid":"76561198848262766"}
}
```
