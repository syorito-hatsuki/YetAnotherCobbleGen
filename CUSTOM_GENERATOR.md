# How to create custom generator

## Folder Structure

```properties
.minecraft
└── config
    └── yacg
        ├── generators
        │   │   # Folder for custom/stick generator. 
        │   │   # Use own unique names instead 
        │   │   # of "custom" and "stick"
        │   ├── custom
        │   │   └── stick
        │   │       ├── textures
        |   |       |   |   # "off" folded is for alternative textures
        |   |       |   |   # used when generator disabled
        |   |       |   |   # (OPTIONAL)
        |   |       |   ├── off
        |   │       |   │   ├── back.png
        |   │       |   │   ├── bottom.png
        |   │       |   │   ├── front.png
        |   │       |   │   ├── left.png
        |   │       |   │   ├── right.png
        |   │       |   │   └── top.png
        │   │       │   ├── back.png
        │   │       │   ├── bottom.png
        │   │       │   ├── front.png
        │   │       │   ├── left.png
        │   │       │   ├── right.png
        │   │       │   └── top.png
        │   │       └── config.json
        │   │   # Yet Another Cobble Generator stock configs
        │   └── yacg
        │       ├── cobble
        │       │   └── config.json
        │       ├── ore
        │       │   └── config.json
        │       └── stone
        │           └── config.json
        └── upgrades.json
```

#### Files

> <details>
> 
> <summary>config.json</summary>
> 
> ```json5
> {
>     // How much energy required for generating
>     "energyUsage": 0,
>     // List of items that will generate
>     "items": {
>         // Item id (can be saw using advanced tooltip)
>         "minecraft:cobblestone": {
>             // the higher the number, 
>             // the higher the chance of a drop
>             // Max is sum value of all weight =< 2,147,483,647
>             "weight": 100,
>             "count": 1
>         },
>         "minecraft:cobbled_deepslate": {
>             "weight": 30,
>             "count": 1
>         },
>         "minecraft:mossy_cobblestone": {
>             "weight": 10,
>             "count": 1
>         }
>     }
> }
> ```
> 
> </details>

