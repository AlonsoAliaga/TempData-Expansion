PlaceholderAPI expansion to generate data for temporary access using placeholders.
# TempData-Expansion
This is a [PlaceholderAPI](https://links.alonsoaliaga.com/PlaceholderAPI) expansion to let owners store temporary data to use.

# Installation
You can install this expansion in 2 ways:
### 1) PlaceholderAPI eCloud (Pending approval ✔️)
While being in console or having OP run the following commands:
> /papi ecloud download tempdata\
> /papi reload

✅ Expansion is ready to be used!
### 2) Manual download
Go to [eCloud](https://api.extendedclip.com/expansions/tempdata/) and click `Download Latest` button to get the .jar file.\
Copy and paste the file in `/plugins/PlaceholderAPI/expansions/` and run:
> /papi reload

✅ Expansion is ready to be used!
# Placeholders
The following placeholders are available:
## %tempdata_playerset_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to save the result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}` part for the player<br>
under the specified `IDENTIFIER` until it's overwritten.<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_playerset_arandomnumber_{rng_1_5}%<br>
**Output:** `3` #This is a random generated number stored for player with identifier `arandomnumber`.

## %tempdata_playerget_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to get the temporary data previously stored under the specified `IDENTIFIER`.<br>
If not available, returns result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}` part for the player.<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_playerget_arandomnumber_{rng_1_5}%<br>
**Output:** `3` #This is the value stored for player with identifier `arandomnumber`.

## %tempdata_playergetorset_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to get the result data previously stored under the specified `IDENTIFIER`.<br>
If not available, stores a new record and returns result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}`<br>
part for the player<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_playergetorset_arandomnumber_{rng_1_5}%<br>
**Output:** `2` #This is a random generated number stored for player with identifier `arandomnumber`.

## %tempdata_globalset_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to save the result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}` part as global data<br>
under the specified `IDENTIFIER` until it's overwritten.<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_globalset_arandomnumber_{rng_1_5}%<br>
**Output:** `4` #This is a random generated number stored in global context with identifier `arandomnumber`.

## %tempdata_globalget_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to get the temporary data previously stored under the specified `IDENTIFIER`.<br>
If not available, returns result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}` part.<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_playerget_arandomnumber_{rng_1_5}%<br>
**Output:** `4` #This is the value stored in global context with identifier `arandomnumber`.

## %tempdata_globalgetorset_&lt;IDENTIFIER&gt;\_{TEXT_OR_PLACEHOLDER_TO_STORE}%
Allows you to get the temporary data previously stored under the specified `IDENTIFIER`.<br>
If not available, stores a new record and returns result data from `{TEXT_OR_PLACEHOLDER_TO_STORE}` part.<br>
<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>
If %placeholder% is required you can use `<percent>` as if it was `%`.<br>
<br>
The example requires [RNG](https://api.extendedclip.com/expansions/rng/) expansion to generate a random number.<br>
**Example:** %tempdata_globalgetorset_arandomnumber_{rng_1_5}%<br>
**Output:** `1` #This is a random generated number stored in global context with identifier `arandomnumber`.

# How is this useful?
Well, I created this because some times you want to "keep" a certain value and re-use it for conditions.<br>
**For example:** When using the plugin ConditionalEvents, you can create a conditions to make something<br>
occurs with specific chances, sadly if you set the conditions to something like:<br>
```yaml
  conditions:
  - "%random_1_5% == 1 execute actionfor1"
  - "%random_1_5% == 2 execute actionfor2"
  - "%random_1_5% == 3 execute actionfor3"
  - "%random_1_5% == 4 execute actionfor4"
  - "%random_1_5% == 5 execute actionfor5"
```
It will create a new random number between 1 and 5 every time it's checked. Which is not what we want!<br>
This example requires the PlaceholderAPI expansion called [RNG](https://api.extendedclip.com/expansions/rng/).<br>
How to use TempData-Expansion to solve this issue? We simply modify our previous code like:
```yaml
  conditions:
  - "%tempdata_playerset_arandomnumber_{rng_1_5}% == 1 execute actionfor1"
  - "%tempdata_playerget_arandomnumber_{rng_1_5}% == 2 execute actionfor2"
  - "%tempdata_playerget_arandomnumber_{rng_1_5}% == 3 execute actionfor3"
  - "%tempdata_playerget_arandomnumber_{rng_1_5}% == 4 execute actionfor4"
  - "%tempdata_playerget_arandomnumber_{rng_1_5}% == 5 execute actionfor5"
  actions:
    actionfor1:
    - "message: &eThe selected random number was &a%tempdata_playerget_arandomnumber_{rng_1_5}%&e!"
```
Another example:
```yaml
  conditions:
  - "%tempdata_playerset_arandomchance_{rng_0_100}% <= 5 execute actionlessthan5"
  - "%tempdata_playerget_arandomchance_{rng_0_100}% <= 15 execute actionlessthan15"
  - "%tempdata_playerget_arandomchance_{rng_0_100}% <= 30 execute actionlessthan30"
  actions:
    actionlessthan5:
    - "message: &eYou have received a &6Legendary &ereward with a probability &aless than 5%&e!"
    actionlessthan15:
    - "message: &eYou have received a &9Rare &ereward with a probability &aless than 15%&e!"
    actionlessthan30:
    - "message: &eYou have received a &aCommon &ereward with a probability &aless than 30%&e!"
```
This will make the code to use the same value stored as `arandomnumber` for the player to persist between<br>
lines, in this case conditions. You can also use it in actions section.<br>
<h2 align="center">
⚠️Notice that the first line uses set and the rest uses get.
</h2>

# Want more cool and useful expansions?
<p align="center">
    <a href="https://alonsoaliaga.com/moregradients">MoreGradients Expansion</a><br>
    Create gradients easily for your texts with hex support!<br>
    <br>
    <a href="https://alonsoaliaga.com/translatefont">TranslateFont Expansion</a><br>
    Translate your text and make them look fancy! ᴍᴀɴʏ ғᴏɴᴛs ᴀᴠᴀɪʟᴀʙʟᴇ!<br>
    <br>
    <a href="https://alonsoaliaga.com/capitalize">Capitalize Expansion</a><br>
    Customize texts a bit more removing underscores, dashes and capitalizing letters!<br>
    <br>
    <a href="https://alonsoaliaga.com/checkmoney">CheckMoney Expansion</a><br>
    Check if player has enough funds or not with custom output! (Specially for menu plugins)<br>
    <br>
    <a href="https://alonsoaliaga.com/checkdate">CheckDate Expansion</a><br>
    Check if server/machine date is the desired one with custom output! (Specially for messages)<br>
</p>

# Want more tools?
**Make sure to check our [BRAND NEW TOOL](https://alonsoaliaga.com/hex) to generate your own text with gradients!**<br>
<p align="center">
    <a href="https://alonsoaliaga.com/hex"><img src="https://i.imgur.com/766Es8I.png" alt="Our brand new tool!"/></a>
</p>

# Do you have a cool expansion idea?
<p align="center">
    <a href="https://alonsoaliaga.com/discord">Join us on Discord</a><br>
    <a href="https://alonsoaliaga.com/discord"><img src="https://i.imgur.com/2pslxIN.png"></a><br>
    Let us know what's your idea and it might become true!
</p>

# Questions?
<p align="center">
    <a href="https://alonsoaliaga.com/discord"><img style="width:200px;" src="https://i.imgur.com/laEFHcG.gif"></a><br>
    <a href="https://alonsoaliaga.com/discord"><span style="font-size:28px;font-weight:bold;color:rgb(100,100,255);">Join us in our discord!</span></a>
</p>