# Fantasy Turn Based Game
What is this? this is a turn based game if you haven't figured it out yet, in this game you can choose 3 classes to start with: Hero, Mage and Thief!
Each class has their own set of skills and stat growths, and that covers that. Next is enemies! Enemies in this game contain AI capable of making decisions inside their boundaries, what do I mean by boundaries? Well assume the enemy doesn't have enough mana for using any skill, no problem! The enemy will look for the most optimal case in this scenario such as a normal attack!
I believe you can figure out the rest yourself... for example if you keep leveling up you'll even notice you can obtain skills, neat!
Anyway, have fun! 

Important notes:
*For enemy AI return values: 0 means no mana, -1 means stat buff used, -2 means the enemy ran out of mana for every skill, so it will use a physical attack,-7 means stat buff can't be used yet
*For User: 0 means no mana, -1 means stat buff used (or healing effect), -2 means stat buff can't be used but wastes a turn nonetheless,
-4 means wrong choice by user.
*For comfortable text speed set Thread.sleep to 0 on line 193 

Things to add later:
1. A back button when choosing to attack or use a skill
2. Implementation of the skill speed
3. An exp multiplier, for both gaining exp and assigning exp to monsters corresponding their lvl