package com.displaynamedisguiser;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("displaynamedisguiser")
public interface DisplayNameDisguiserConfig extends Config
{
	@ConfigSection(
			name = "Show Name List (Self)",
			description = "Shows the text box to customize the names for yourself.",
			position = 1,
			closedByDefault = true
	)
	String nameSection = "names";
	@ConfigSection(
			name = "Show Name List (Others)",
			description = "Shows the text box to customize the names for others.",
			position = 4
	)
	String nameOthersSection = "namesOthers";
	@ConfigItem(
			keyName = "selfToggle",
			name = "Change Name for Self?",
			description = "Toggle changing the display name for Yourself.",
			position = 0
	)
	default boolean changeSelf()
	{
		return true;
	}
	@ConfigItem(
			keyName = "selfNames",
			name = "Display Names for Self",
			description = "Set the list of possible display names to choose from. Separate each name with a comma (,)",
			position = 2,
			section = nameSection
	)
	default String selfNameList()
	{
		return "Hydra,Vanguard,Rocco,Blizzard,Reverie,Neptune,Harbinger,Blaze,Nightshade,Elysium,Astral,Zulrah,Vortex,Cedar,Arcadia,Chronicle,Chloe,Sky,Hank,Dragon,Phantom,Tekton,Elysian,Vitur,Piper,Oscar,Pluto,Muttadile,Blossom,Finn,Electra,Celestine,Trinity,Luminescence,Velvet,Gus,Tucker,Millie,Baron,River,Reed,Harmony,Argent,Spirit,Lola,Oda,Aetherial,Neon,Storm,Murphy,Meridian,Apogee,Rose,Copper,Vorkath,Infinity,Arcane,Juniper,Milo,Echo,Moss,Sirocco,Lexi,Drifter,Cooper,Spectrum,Charlie,Holly,Wildflower,Bella,Bear,Callisto,Inferno,Gizmo,Aurelia,Apollo,Vesper,Ocean,Jasper,Enigma,Pearl,Nova,Mystic,Spectre,Lucy,Callie,Aetherium,Valkyrie,Chrysalis,Scorpia,Vardorvis,Mercury,Sentinel,Chronos,Maelstrom,Ice,Ember,Adonis,Stella,Rat,Otis,Apex,Aquila,Midnight,Mirage,Siren,Brook,Aether,Xarpus,Vespula,Winston,Alchemist,Demon,Harper,Comet,Pepper,Willow,Celestia,Azura,Ollie,Cody,Orchid,Iris,Sydney,Aphelion,Mythos,Mist,Sam,Mystique,Maple,Meadow,Enchanter,Obor,Seraphim,Marley,Riley,Jad,Hazel,Harley,Jax,Heather,Alchemy,Rain,Wanderer,Minnie,Euphoria,Bailey,Coco,Eclipse,Partake,Brock,Salty,JB,Dirty,Marner,Taco,Blueberry,Gorilla,Selene,Ivy,Orion,Artemis,Aeon,Ashes,Abyss,Veritas,Archie,Aspen,Lulu,Kraken,Sophie,Myriad,Cosmos,Luminaire,Max,Sage,Teddy,Violet,Aura,Aurora,Astrum,Atlas,Cleo,Bruce,Thyme,Orbit,Luminary,Tranquility,Peanut,Ella,Serenade,Dawn,Leo,Azure,Whisperer,Toby,Quintessence,Labyrinth,Roxy,Rusty,Trixie,Empyrean,Verzik,Lily,Scout,Abby,Whimsy,Vetion,Olm,Ginger,Quasar,Moose,Zoey,Mia,Seraph,Silhouette,Nala,Paradox,Jake,Leaf,Joe,Boomer,Skotizo,Obsidian,Stellar,Jack,Elm,Zenith,Sotetseg,Layla,Rowan,Sapphire,Astra,Dexter,Genesis,Aquarius,Saturn,Venus,Bentley,Maggie,Buddy,Magnus,Duke,Nylocas,Rocky,Paragon,Anomaly,Baxter,Gracie,Firefly,Izzy,Bloat,Scarlet,Oblivion,Celestial,Sasha,Ascendant,Buster,Moonlight,Chimera,Sadie,Azimuth,Chase,Solstice,Evergreen,Panorama,Fiesta,Sunwise,Grain,Harvest,Portal,Bay,Bow,Kaleido,Polar,Odyssey,Zing,Captain,Zeta,Gem,Cosmic,Story,Falcon,Ideal,Hero,Harbor,Sigma,Vision,Chi,Lambda,Rocket,Stone,Romance,Cyber,Soleil,Caravan,Instinct,Millen,Jade,Flare,Shard,Bloom,Bright,Marble,Fog,Hydra,Ethereal,Relic,Upswing,Morning,Fawn,Regal,Rapids,Antics,Bonanza,Zest,Master,Fortune,Teal,Quill,Bounty,Boost,Limelight,Titan,Tower,New,Energy,Starshine,Trend,Epic,Legacy,Corona,Noble,Magic,Silver,Journey,Sugar,Blue,Fern,Fire,Daisy,Dash,Star,Wild,Marvel,Purple,Clone,Summit,Cadet,Sol,Omicron,Sunrise,Grand,Psi,Terra,Heart,Guide,Newborn,Cherub,Universe,Basil,Buzz,Mingle,Beyond,Zen,Green,Clarity,Redwood,Calm,Daybreak,Upsilon,Aria,Tribe,Upbeat,Wonder,Song,Stardust,Vivid,Flicker,Enterprise,Supreme,Delta,Novel,Forge,Vista,Skyline,Joy,Travel,Gentle,Curve,Fantasy,Myth,Rho,Luna,Sunbeam,Ridge,Jubilee,Legend,Emerald,Misty,Merlin,Flash,Fox,Quiver,Sonar,Nest,Iota,Lagoon,Shade,Century,Omega,Sundance,Bee,Circuit,Forward,Modern,Glimmer,Water,Knight,Starwood,Verse,Surf,Powder,Theta,Aristo,Winter,Crest,Whisper,Justice,Synergy,Thunder,Herald,Shadow,Sweet,Blade,Companion,Arrow,Pollen,Dynamo,Union,Acorn,Ultimate,Zodiac,Sunwood,Fathom,Royal,Fireside,Babble,Grove,Bridge,Pixel,Uplift,Fresh,Satellite,Avalon,Parallel,Serenity,Mint,Sailor,Sunwave,Tan,Eta,Data,Edge,Sunshine,Adventure,Sunstone,Epsilon,Planet,Haven,Platinum,Villa,Conquest,Flame,Guardian,Tulip,Ace,Pleasure,Nebula,Xi,Gamma,Strong,Imagine,Steam,Aviator,Lux,Ivory,Sun,Tropic,Iron,Skylark,Majesty,Solar,Cupid,Kappa,Simba,Equinox,Bandit,Tree,Bison,Phoenix,Pioneer,Horizon,Gold,Starlight,World,Light,Remedy,Vanilla,Desire,Signal,Windsor,Vital,Phi,Rainbow,Oracle,Paradise,Mu,Quest,Tidal,Evolve,Eagle,Valiant,Newlife,Treasure,Crimson,Nu,Tau,Kingdom,Marine,Fleet,Surreal,Prosper,Moon,Torch,Pathway,Resonance,Thrive,Vector,Fusion,Golden,Wrap,Prism,Unity,Snow,Tiger,Zombie,Castle,Jupiter,Petal,Insight,Tranquil,Earth,Whale,Citadel,Berry,Mirror,Apple,Newday,Shine,Pulse,Shore,Crystal,Admiral,Splash,Spire,Giggle,Luxury,Mountain,Glimpse,Transpire,Contour,Solaris,Concord,Sundown,Trail,Miracle,Starstruck,Sunset,Empire,Archer,Cascade,Avenue,Breeze,Constel,Voyage,Reflect,Zephyr,Ruby,Lush,Bolt,Vanity,Glory,Amber,Matrix,Worthy,Abacus,Quicksilver,Melody,True,Gravity,Summer,Pure,Transit,Champion,Vibrant,Revolve,Inspire,Beta,Sleek,Liberty,Vigor,Radiant,Heaven,Chaos,Halo,Zion,Anchor,Destiny,Pi,Wander,Diamond,Warrior,View,Gust,Lark,Peach,Soar,Nexus,Gift,Impact,Raven,Saga,Command,Outlook,Cloud,Core,Rebirth,Action,Talon,Hunter,Origin,Passion,Lion,Marina,Asteroid,Victory,Mesh,Pastel,Citizen,Mind,Athena,Vertex,Wisdom,Alpha,Silk,Infinite,Liber,Monarch,Cluster,Banner,Grace,Ripple,Flight,Triton,Raindrop,Soul,Strike,Rise,Wood,Spark,Galaxy";
	}
	@ConfigItem(
			keyName = "othersToggle",
			name = "Change Name for Others?",
			description = "Toggle changing the display name for Other Players.",
			position = 3
	)
	default boolean changeOthers()
	{
		return false;
	}
	@ConfigItem(
			keyName = "othersNames",
			name = "Display Names for Others",
			description = "Set the display name for another player. The format is OldName:NewName with each person per line.",
			position = 5,
			section = nameOthersSection
	)
	default String otherNameList()
	{
		return "Old Name:New Name" + "\n" + "Zezima:Not Zezima" + "\n" + "xXDragonSlay3rXx:Dragon Slayer" + "\n" + "TurboWet:Charlie";
	}
	@ConfigItem(
			keyName = "generateKey",
			name = "Click to Create Key (Clipboard)->",
			description = "Generate a new obfuscation key to your clipboard. Paste into Secret Key textbox before enabling Secret Mode.",
			position = 6
	)
	default boolean generateKey()
	{
		return false;
	}
	@ConfigItem(
			keyName = "obfuscationKey",
			name = "Secret Key",
			secret = true,
			description = "The secret obfuscation key, do not touch.",
			position = 7
	)
	default String obfuscationKey()
	{
		return "h9TIlznNraYkB2vEDd6Apj4Wx8bSJ3P7OyqKQfsZM1GCouXUiRVLH5mt0Fwceg";
	}
	@ConfigItem(
			keyName = "obfuscationToggle",
			name = "Secret Mode?(Copy to Clipboard)",
			description = "Toggle obfuscating display name changes for Others. Paste your clipboard in the Display Names for Others textbox.",
			position = 8
	)
	default boolean obfuscateOthers()
	{
		return false;
	}
	@ConfigItem(
			name = "Hide in widgets (Lag warning)",
			keyName = "hideWidgets",
			description = "Hides your Display Name everywhere. Might lag your game.",
			position = 9
	)
	default boolean hideWidgets()
	{
		return false;
	}
}