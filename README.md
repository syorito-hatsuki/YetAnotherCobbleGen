<!-- Suppress IDEA Warnings -->
<!--suppress ALL -->

<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Discord][discord-shield]][discord-url]
[![Modrinth][modrinth-shield]][modrinth-url]

<br />
<div align="center">
  <a href="https://github.com/syorito-hatsuki/yet-another-cobble-gen">
    <img src="https://github.com/syorito-hatsuki/yet-another-cobble-gen/blob/1.20/src/main/resources/assets/yacg/icon.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Yet Another Cobblestone Generator</h3>

  <p align="center">
    Flexible customizable generators
    <br />
    <a href="https://discord.gg/pbwnMwnUD6">Support</a>
    ·
    <a href="https://github.com/syorito-hatsuki/yet-another-cobble-gen/issues">Report Bug</a>
    ·
    <a href="https://github.com/syorito-hatsuki/yet-another-cobble-gen/issues">Request Feature</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#gameplay">Gameplay</a></li>
        <li><a href="#crafts">Crafts</a></li>
        <li><a href="#integrations">Integrations</a>
                <ul>
                  <li><a href="#rei">REI</a></li>
                  <li><a href="#emi">EMI</a></li>
                  <li><a href="#jade">Jade</a></li>
                </ul>
              </li>
        <li><a href="#config">Config</a></li>
        </ul>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

## About The Project

![In-Game ScreenShot][screenshot]

A cobblestone generator, a classic of any SkyBlock. However, I'm bored with classic generators that generate only 1 kind
of block. That's why I made my own mod, with a wide range of customization options.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* ![Fabric][fabric]
* ![Fabric-Language-Kotlin][fabric-language-kotlin]
* ![ModMenu Badges Lib][modmenu-badges-lib]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

### Gameplay

* Right-click to get info about what is already generated
* Shift + Right-click to collect all from the generator
* Shift + Right-click to put upgrade item into generator

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Crafts

| ![Cobblestone Generator Craft](./assets/recipes/cobble.png)    | ![Ore Generator Craft](./assets/recipes/ore.png)   | ![Stone Generator Craft](./assets/recipes/stone.png)           |
|----------------------------------------------------------------|----------------------------------------------------|----------------------------------------------------------------|
| ![Speed Upgrade Craft](./assets/recipes/speed.png)             | ![Count Upgrade Craft](./assets/recipes/count.png) | ![Coefficient Upgrade Craft](./assets/recipes/coefficient.png) |
| ![Energy Free Upgrade Craft](./assets/recipes/energy_free.png) |                                                    |                                                                |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Integrations

#### REI

![REI Integration ScreenShot][rei-integration]

#### EMI

![EMI Integration ScreenShot][emi-integration]

#### Jade

![Jade Integration ScreenShot][jade-integration]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Config

<details>
  <summary>Generators default config</summary>

```json
[
    {
        "type": "cobble",
        "energyUsage": 0,
        "items": [
            {
                "itemId": "minecraft:cobblestone",
                "coefficient": 100,
                "count": 1
            },
            {
                "itemId": "minecraft:cobbled_deepslate",
                "coefficient": 30,
                "count": 1
            },
            {
                "itemId": "minecraft:mossy_cobblestone",
                "coefficient": 10,
                "count": 1
            }
        ]
    },
    {
        "type": "ore",
        "energyUsage": 100,
        "items": [
            {
                "itemId": "minecraft:coal_ore",
                "coefficient": 100,
                "count": 1
            },
            {
                "itemId": "minecraft:copper_ore",
                "coefficient": 70,
                "count": 1
            },
            {
                "itemId": "minecraft:iron_ore",
                "coefficient": 50,
                "count": 1
            },
            {
                "itemId": "minecraft:gold_ore",
                "coefficient": 30,
                "count": 1
            },
            {
                "itemId": "minecraft:redstone_ore",
                "coefficient": 20,
                "count": 1
            },
            {
                "itemId": "minecraft:lapis_ore",
                "coefficient": 20,
                "count": 1
            },
            {
                "itemId": "minecraft:diamond_ore",
                "coefficient": 15,
                "count": 1
            },
            {
                "itemId": "minecraft:emerald_ore",
                "coefficient": 10,
                "count": 1
            },
            {
                "itemId": "minecraft:nether_quartz_ore",
                "coefficient": 5,
                "count": 1
            }
        ]
    },
    {
        "type": "stone",
        "energyUsage": 0,
        "items": [
            {
                "itemId": "minecraft:stone",
                "coefficient": 100,
                "count": 1
            },
            {
                "itemId": "minecraft:diorite",
                "coefficient": 50,
                "count": 1
            },
            {
                "itemId": "minecraft:granite",
                "coefficient": 50,
                "count": 1
            },
            {
                "itemId": "minecraft:andesite",
                "coefficient": 50,
                "count": 1
            },
            {
                "itemId": "minecraft:calcite",
                "coefficient": 20,
                "count": 1
            },
            {
                "itemId": "minecraft:dripstone_block",
                "coefficient": 20,
                "count": 1
            },
            {
                "itemId": "minecraft:deepslate",
                "coefficient": 5,
                "count": 1
            }
        ]
    }
]
```
  
</details>

<details>
  <summary>Generators default config (Before 2024.5.2)</summary>

```json
{
  "generators": {
    "cobble": [
      {
        "itemId": "minecraft:cobblestone",
        "coefficient": 100,
        "count": 1
      },
      {
        "itemId": "minecraft:cobbled_deepslate",
        "coefficient": 30,
        "count": 1
      },
      {
        "itemId": "minecraft:mossy_cobblestone",
        "coefficient": 10,
        "count": 1
      }
    ],
    "ore": [
      {
        "itemId": "minecraft:coal_ore",
        "coefficient": 0,
        "count": 1
      },
      {
        "itemId": "minecraft:copper_ore",
        "coefficient": 70,
        "count": 1
      },
      {
        "itemId": "minecraft:iron_ore",
        "coefficient": 50,
        "count": 1
      },
      {
        "itemId": "minecraft:gold_ore",
        "coefficient": 30,
        "count": 1
      },
      {
        "itemId": "minecraft:redstone_ore",
        "coefficient": 20,
        "count": 1
      },
      {
        "itemId": "minecraft:lapis_ore",
        "coefficient": 20,
        "count": 1
      },
      {
        "itemId": "minecraft:diamond_ore",
        "coefficient": 15,
        "count": 1
      },
      {
        "itemId": "minecraft:emerald_ore",
        "coefficient": 10,
        "count": 1
      },
      {
        "itemId": "minecraft:nether_quartz_ore",
        "coefficient": 5,
        "count": 1
      }
    ],
    "stone": [
      {
        "itemId": "minecraft:stone",
        "coefficient": 100,
        "count": 1
      },
      {
        "itemId": "minecraft:diorite",
        "coefficient": 50,
        "count": 1
      },
      {
        "itemId": "minecraft:granite",
        "coefficient": 50,
        "count": 1
      },
      {
        "itemId": "minecraft:andesite",
        "coefficient": 50,
        "count": 1
      },
      {
        "itemId": "minecraft:calcite",
        "coefficient": 20,
        "count": 1
      },
      {
        "itemId": "minecraft:dripstone_block",
        "coefficient": 20,
        "count": 1
      },
      {
        "itemId": "minecraft:deepslate",
        "coefficient": 5,
        "count": 1
      }
    ]
  }
}
```

</details>


<details>
    <summary>Upgrades default config</summary>

```json
{
  "COUNT": 2,
  "COEFFICIENT": 2,
  "SPEED": 2
}
```

</details>

If u want to add own generator [go here](./CUSTOM_GENERATOR.md)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Roadmap

- [x] Customizable generators
- [x] Generator upgrades
- [x] EMI Integration
- [x] Jade Integration
- [x] REI Integration

See the [open issues](https://github.com/syorito-hatsuki/yet-another-cobble-gen/issues) for a full list of proposed
features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[contributors-shield]: https://img.shields.io/github/contributors/syorito-hatsuki/yet-another-cobble-gen.svg?style=for-the-badge

[contributors-url]: https://github.com/syorito-hatsuki/yet-another-cobble-gen/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/syorito-hatsuki/yet-another-cobble-gen.svg?style=for-the-badge

[forks-url]: https://github.com/syorito-hatsuki/yet-another-cobble-gen/network/members

[stars-shield]: https://img.shields.io/github/stars/syorito-hatsuki/yet-another-cobble-gen.svg?style=for-the-badge

[stars-url]: https://github.com/syorito-hatsuki/yet-another-cobble-gen/stargazers

[issues-shield]: https://img.shields.io/github/issues/syorito-hatsuki/yet-another-cobble-gen.svg?style=for-the-badge

[issues-url]: https://github.com/syorito-hatsuki/yet-another-cobble-gen/issues

[license-shield]: https://img.shields.io/github/license/syorito-hatsuki/yet-another-cobble-gen.svg?style=for-the-badge

[license-url]: https://github.com/syorito-hatsuki/yet-another-cobble-gen/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://linkedin.com/in/kit-lehto

[screenshot]: https://cdn-raw.modrinth.com/data/xPsKRMUF/images/047a2072ffe8fe5368479d0560eb2bbca2b1ef5f.png

[rei-integration]: https://cdn.modrinth.com/data/xPsKRMUF/images/8728ef2189b41115e4c77036d2bf997e44dea665.png

[emi-integration]: https://cdn.modrinth.com/data/xPsKRMUF/images/3a52d6a7b75930e96866daba0f51d3b52db020da.png

[jade-integration]: https://cdn.modrinth.com/data/xPsKRMUF/images/89bce38efe2b0da0e5b0805a483c2ede7706edd9.png

[fabric]: https://img.shields.io/badge/fabric%20api-DBD0B4?style=for-the-badge

[fabric-language-kotlin]: https://img.shields.io/badge/fabric%20language%20kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white

[modmenu-badges-lib]: https://img.shields.io/badge/modmenu%20badges%20lib-434956?style=for-the-badge

[discord-shield]: https://img.shields.io/discord/1032138561618726952?logo=discord&logoColor=white&style=for-the-badge&label=Discord

[discord-url]: https://discord.gg/pbwnMwnUD6

[modrinth-shield]: https://img.shields.io/modrinth/v/yacg?label=Modrinth&style=for-the-badge

[modrinth-url]: https://modrinth.com/mod/yacg
