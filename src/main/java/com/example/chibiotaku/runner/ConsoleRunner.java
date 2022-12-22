package com.example.chibiotaku.runner;

import com.example.chibiotaku.constraints.ErrorMessage;
import com.example.chibiotaku.domain.entities.AnimeEntity;
import com.example.chibiotaku.domain.entities.MangaEntity;
import com.example.chibiotaku.domain.entities.RoleEntity;
import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.domain.enums.AnimeTypeEnum;
import com.example.chibiotaku.domain.enums.RoleUserEnum;
import com.example.chibiotaku.domain.enums.StatusObjectEnum;
import com.example.chibiotaku.repo.*;
import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private UserRepository userRepository;
    private AnimeRepository animeRepository;
    private MangaRepository mangaRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public ConsoleRunner(UserRepository userRepository,
                         AnimeRepository animeRepository,
                         MangaRepository mangaRepository, RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.animeRepository = animeRepository;
        this.mangaRepository = mangaRepository;

        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        initRole();
        initUser();
        initAnimes();
        initMangas();




    }
   private void initRole(){
        if (roleRepository.count() == 0){
            RoleEntity adminRole = new RoleEntity()
                    .setRole(RoleUserEnum.ADMIN);
            RoleEntity userRole = new RoleEntity()
                    .setRole(RoleUserEnum.USER);
            this.roleRepository.saveAll(List.of(adminRole,userRole));
        }
    }
   private void initUser() {

        RoleEntity userRole = this.roleRepository.findRoleEntityByRole(RoleUserEnum.USER)
                .orElseThrow(() -> new ObjectNotFoundException("Role with name" + RoleUserEnum.USER.name() + "was not found"));
        RoleEntity adminRole = this.roleRepository.findRoleEntityByRole(RoleUserEnum.ADMIN)
                .orElseThrow(() -> new ObjectNotFoundException("Role with name" + RoleUserEnum.ADMIN.name() + "was not found"));


        if (userRepository.count() == 0) {

            UserEntity user1 = new UserEntity()
                    .setUsername("chris96")
                    .setCreated(LocalDate.now())
                    .setFirstName("Christian")
                    .setLastName("Smith")
                    .setPassword(passwordEncoder.encode("love123"))
                    .setEmail("chris@none.com")
                    .setRoles(List.of(userRole));

            UserEntity user2 = new UserEntity()
                    .setUsername("alex96")
                    .setCreated(LocalDate.now())
                    .setFirstName("Alexander")
                    .setLastName("Jeff")
                    .setPassword(passwordEncoder.encode("somepw534"))
                    .setEmail("alex@none.com")
                    .setRoles(List.of(userRole));
            UserEntity admin = new UserEntity()
                    .setUsername("admin")
                    .setCreated(LocalDate.now())
                    .setFirstName("Admin")
                    .setLastName("Admin")
                    .setPassword(this.passwordEncoder.encode("topsecret"))
                    .setEmail("admin@ex.com")
                    .setRoles(List.of(userRole, adminRole));

            this.userRepository.saveAll(List.of(user1, user2, admin));
        }
    }
   private void initAnimes(){
        String usernameAdmin = "admin";
        UserEntity admin = this.userRepository.findUserEntityByUsername(usernameAdmin).
                orElseThrow( () -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,usernameAdmin)));
        if (animeRepository.count() == 0){
            AnimeEntity naruto = new AnimeEntity()
                    .setTitle("Naruto")
                    .setAired(LocalDate.of(2002,10,3))
                    .setEpisodes(220)
                    .setProducers("TV Tokyo, Aniplex, Shueisha")
                    .setStudios("Pierrot")
                    .setSource("Manga")
                    .setGenres("Action, Adventure, Fantasy")
                    .setDuration(24)
                    .setRating("PG-13 - Teens 13 or older")
                    .setRank(8)
                    .setMembers(2603888)
                    .setPopularity(100)
                    .setContent("Moments prior to Naruto Uzumaki's birth, a huge demon known as the Kyuubi, the Nine-Tailed Fox, attacked Konohagakure, the Hidden Leaf Village, and wreaked havoc. In order to put an end to the Kyuubi's rampage, the leader of the village, the Fourth Hokage, sacrificed his life and sealed the monstrous beast inside the newborn Naruto." +
                            "Now, Naruto is a hyperactive and knuckle-headed ninja still living in Konohagakure. Shunned because of the Kyuubi inside him, Naruto struggles to find his place in the village, while his burning desire to become the Hokage of Konohagakure leads him not only to some great new friends, but also some deadly foes.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/13/17405l.jpg")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());

            AnimeEntity fairyTail = new AnimeEntity()
                    .setTitle("Fairy Tail (2014)")
                    .setAired(LocalDate.of(2014,4,26))
                    .setEpisodes(102)
                    .setProducers("TV Tokyo, Dentsu")
                    .setStudios("A-1 Pictures, Bridge")
                    .setSource("Manga")
                    .setGenres("Action, Adventure, Fantasy")
                    .setDuration(24)
                    .setRating("PG-13 - Teens 13 or older")
                    .setRank(600)
                    .setMembers( 919323)
                    .setPopularity(150)
                    .setContent("The Grand Magic Games reaches its climax following Natsu Dragneel and Gajeel Redfox's stunning victory over Sting Eucliffe and Rogue Cheney of the Sabertooth guild. This success pushes the Fairy Tail guild closer to being crowned the overall champions, but obtaining victory isn't the only challenge they face. A mystery still surrounds a hooded stranger and the ominous Eclipse Gate, leaving more questions than answers.\n" +
                            "\n" +
                            "More crazy adventures are on the horizon for Fairy Tail as their destructive antics and joyful rowdiness continue unabated. Their greatest trial is quickly approaching, but united as a family, the guild will always be ready to face any threat that comes their way.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/3/60551l.jpg?_gl=1*77qcvv*_ga*MTI2NDA5MTI0MS4xNjY5OTg2MTI0*_ga_26FEP9527K*MTY3MDE2NjU2NC4zLjEuMTY3MDE2NjcyMy42MC4wLjA.")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());

            AnimeEntity narutoShippuuden = new AnimeEntity()
                    .setTitle("Naruto Shippuden")
                    .setAired(LocalDate.of(2007,3,23))
                    .setEpisodes(500)
                    .setProducers("TV Tokyo, Aniplex, KSS, Rakuonsha, TV Tokyo Music, Shueisha")
                    .setStudios("Pierrot")
                    .setSource("Manga")
                    .setGenres("Action, Adventure, Fantasy")
                    .setDuration(24)
                    .setRating("PG-13 - Teens 13 or older")
                    .setRank(284)
                    .setMembers(919323)
                    .setPopularity(284)
                    .setContent("The Grand Magic Games reaches its climax following Natsu Dragneel and Gajeel Redfox's stunning victory over Sting Eucliffe and Rogue Cheney of the Sabertooth guild. This success pushes the Fairy Tail guild closer to being crowned the overall champions, but obtaining victory isn't the only challenge they face. A mystery still surrounds a hooded stranger and the ominous Eclipse Gate, leaving more questions than answers.\n" +
                            "\n" +
                            "More crazy adventures are on the horizon for Fairy Tail as their destructive antics and joyful rowdiness continue unabated. Their greatest trial is quickly approaching, but united as a family, the guild will always be ready to face any threat that comes their way.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/1565/111305.jpg")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());
            AnimeEntity chainsawMan = new AnimeEntity()
                    .setTitle("Chainsaw Man")
                    .setAired(LocalDate.of(2022,10,12))
                    .setEpisodes(12)
                    .setProducers("Ryū Nakayama Masato Nakazono")
                    .setStudios("MAPPA")
                    .setSource("Manga")
                    .setGenres("Action, Fantasy, Shounen")
                    .setDuration(24)
                    .setRating("R - 17+ (violence & profanity)")
                    .setRank(155)
                    .setMembers(894431)
                    .setPopularity(155)
                    .setContent("Denji is robbed of a normal teenage life, left with nothing but his deadbeat father's overwhelming debt. His only companion is his pet, the chainsaw devil Pochita, with whom he slays devils for money that inevitably ends up in the yakuza's pockets. All Denji can do is dream of a good, simple life: one with delicious food and a beautiful girlfriend by his side. But an act of greedy betrayal by the yakuza leads to Denji's brutal, untimely death, crushing all hope of him ever achieving happiness.\n" +
                            "\n" +
                            "Remarkably, an old contract allows Pochita to merge with the deceased Denji and bestow devil powers on him, changing him into a hybrid able to transform his body parts into chainsaws. Because Denji's new abilities pose a significant risk to society, the Public Safety Bureau's elite devil hunter Makima takes him in, letting him live as long as he obeys her command. Guided by the promise of a content life alongside an attractive woman, Denji devotes everything and fights with all his might to make his naive dreams a reality.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/1806/126216.jpg")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());
            AnimeEntity erased = new AnimeEntity()
                    .setTitle("Boku dake ga Inai Machi")
                    .setAired(LocalDate.of(2016,1,8))
                    .setEpisodes(12)
                    .setProducers("Aniplex, Dentsu, Kadokawa Shoten, Fuji TV, DAX Production, Kyoraku Industrial Holdings, Kansai Telecasting, Lawson, Kanetsu Investment, C-one")
                    .setStudios("A-1 Pictures")
                    .setSource("Manga")
                    .setGenres("Mystery, Supernatural")
                    .setDuration(24)
                    .setRating("R - 17+ (violence & profanity)")
                    .setRank(228)
                    .setMembers(1912604)
                    .setPopularity(30)
                    .setContent("When tragedy is about to strike, Satoru Fujinuma finds himself sent back several minutes before the accident occurs. The detached, 29-year-old manga artist has taken advantage of this powerful yet mysterious phenomenon, which he calls \"Revival,\" to save many lives.\n" +
                            "\n" +
                            "However, when he is wrongfully accused of murdering someone close to him, Satoru is sent back to the past once again, but this time to 1988, 18 years in the past. Soon, he realizes that the murder may be connected to the abduction and killing of one of his classmates, the solitary and mysterious Kayo Hinazuki, that took place when he was a child. This is his chance to make things right.\n" +
                            "\n" +
                            "Boku dake ga Inai Machi follows Satoru in his mission to uncover what truly transpired 18 years ago and prevent the death of his classmate while protecting those he cares about in the present.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/10/77957.jpg")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());
            AnimeEntity onePunchMan = new AnimeEntity()
                    .setTitle("One Punch Man")
                    .setAired(LocalDate.of(2015,10,5))
                    .setEpisodes(12)
                    .setProducers("TV Tokyo, Bandai Visual, Lantis, Asatsu DK, Banpresto, Good Smile Company, Shueisha, JR East Marketing & Communications")
                    .setStudios("Madhouse")
                    .setSource("Manga")
                    .setGenres(" Action, Comedy")
                    .setDuration(24)
                    .setRating("R - 17+ (violence & profanity)")
                    .setRank(119)
                    .setMembers(1912604)
                    .setPopularity(4)
                    .setContent("The seemingly unimpressive Saitama has a rather unique hobby: being a hero. In order to pursue his childhood dream, Saitama relentlessly trained for three years, losing all of his hair in the process. Now, Saitama is so powerful, he can defeat any enemy with just one punch. However, having no one capable of matching his strength has led Saitama to an unexpected problem—he is no longer able to enjoy the thrill of battling and has become quite bored.\n" +
                            "\n" +
                            "One day, Saitama catches the attention of 19-year-old cyborg Genos, who witnesses his power and wishes to become Saitama's disciple. Genos proposes that the two join the Hero Association in order to become certified heroes that will be recognized for their positive contributions to society. Saitama, who is shocked that no one knows who he is, quickly agrees. Meeting new allies and taking on new foes, Saitama embarks on a new journey as a member of the Hero Association to experience the excitement of battle he once felt.")
                    .setImageUrl("https://cdn.myanimelist.net/images/anime/7/72533l.jpg")
                    .setUser(admin)
                    .setStatusObject(StatusObjectEnum.ACCEPTED)
                    .setType(AnimeTypeEnum.TV)
                    .setCreated(LocalDate.now());

            this.animeRepository.saveAll(List.of(naruto,narutoShippuuden,fairyTail,onePunchMan,erased,chainsawMan));
        }
    }
   private void initMangas(){
       String usernameAdmin = "admin";
       UserEntity admin = this.userRepository.findUserEntityByUsername(usernameAdmin).
               orElseThrow( () -> new UsernameNotFoundException(String.format(ErrorMessage.USERNAME_NOT_FOUND,usernameAdmin)));

       if(mangaRepository.count() == 0){

           MangaEntity soloLeveling = new MangaEntity()
                   .setTitle("Solo Leveling")
                   .setRanked(45)
                   .setPopularity(1)
                   .setMembers(395058)
                   .setContent("Ten years ago, \"the Gate\" appeared and connected the real world with the realm of magic and monsters. To combat these vile beasts, ordinary people received superhuman powers and became known as \"Hunters.\" Twenty-year-old Sung Jin-Woo is one such Hunter, but he is known as the \"World's Weakest,\" owing to his pathetic power compared to even a measly E-Rank. Still, he hunts monsters tirelessly in low-rank Gates to pay for his mother's medical bills.\n" +
                           "\n" +
                           "However, this miserable lifestyle changes when Jin-Woo—believing himself to be the only one left to die in a mission gone terribly wrong—awakens in a hospital three days later to find a mysterious screen floating in front of him. This \"Quest Log\" demands that Jin-Woo completes an unrealistic and intense training program, or face an appropriate penalty. Initially reluctant to comply because of the quest's rigor, Jin-Woo soon finds that it may just transform him into one of the world's most fearsome Hunters.")
                   .setVolumes("5")
                   .setChapters(180)
                   .setPublished(LocalDate.of(2018,3,4))
                   .setGenres("Action, Adventure, Fantasy")
                   .setRating("Unknown")
                   .setAuthors("Chugong (Story), Jang, Sung-rak (Art)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/3/222295.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());;

           MangaEntity demonSlayer = new MangaEntity()
                   .setTitle("Demon Slayer")
                   .setPopularity(11)
                   .setRanked(329)
                   .setMembers(375937)
                   .setContent("Tanjirou Kamado lives with his impoverished family on a remote mountain. As the oldest sibling, he took upon the responsibility of ensuring his family's livelihood after the death of his father. On a cold winter day, he goes down to the local village in order to sell some charcoal. As dusk falls, he is forced to spend the night in the house of a curious man who cautions him of strange creatures that roam the night: malevolent demons who crave human flesh.\n" +
                           "\n" +
                           "When he finally makes his way home, Tanjirou's worst nightmare comes true. His entire family has been brutally slaughtered with the sole exception of his sister Nezuko, who has turned into a flesh-eating demon. Engulfed in hatred and despair, Tanjirou desperately tries to stop Nezuko from attacking other people, setting out on a journey to avenge his family and find a way to turn his beloved sister back into a human.")
                   .setVolumes("207")
                   .setChapters(180)
                   .setPublished(LocalDate.of(2018,3,4))
                   .setGenres("Action, Fantasy")
                   .setRating("Unknown")
                   .setAuthors("Gotouge, Koyoharu (Story & Art)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/3/179023.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());

           MangaEntity onePiece = new MangaEntity()
                   .setTitle("One Piece")
                   .setPopularity(3)
                   .setRanked(3)
                   .setMembers(533597)
                   .setContent("Gol D. Roger, a man referred to as the \"Pirate King,\" is set to be executed by the World Government. But just before his demise, he confirms the existence of a great treasure, One Piece, located somewhere within the vast ocean known as the Grand Line. Announcing that One Piece can be claimed by anyone worthy enough to reach it, the Pirate King is executed and the Great Age of Pirates begins.\n" +
                           "\n" +
                           "Twenty-two years later, a young man by the name of Monkey D. Luffy is ready to embark on his own adventure, searching for One Piece and striving to become the new Pirate King. Armed with just a straw hat, a small boat, and an elastic body, he sets out on a fantastic journey to gather his own crew and a worthy ship that will take them across the Grand Line to claim the greatest status on the high seas.")
                   .setVolumes("101")
                   .setChapters(1000)
                   .setPublished(LocalDate.of(1999,7,22))
                   .setGenres("Action, Adventure, Fantasy")
                   .setRating("Unknown")
                   .setAuthors("Oda, Eiichiro (Story & Art)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/2/253146.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());
           MangaEntity tokyoGhoul = new MangaEntity()
                   .setTitle("Tokyo Ghoul")
                   .setPopularity(5)
                   .setRanked(116)
                   .setMembers(452800)
                   .setContent("Lurking within the shadows of Tokyo are frightening beings known as \"ghouls,\" who satisfy their hunger by feeding on humans once night falls. An organization known as the Commission of Counter Ghoul (CCG) has been established in response to the constant attacks on citizens and as a means of purging these creatures. However, the problem lies in identifying ghouls as they disguise themselves as humans, living amongst the masses so that hunting prey will be easier. Ken Kaneki, an unsuspecting university freshman, finds himself caught in a world between humans and ghouls when his date turns out to be a ghoul after his flesh.\n" +
                           "\n" +
                           "Barely surviving this encounter after being taken to a hospital, he discovers that he has turned into a half-ghoul as a result of the surgery he received. Unable to satisfy his intense craving for human meat through conventional means, Kaneki is taken in by friendly ghouls who run a coffee shop in order to help him with his transition. As he begins what he thinks will be a peaceful new life, little does he know that he is about to find himself at the center of a war between his new comrades and the forces of the CCG, and that his new existence has caught the attention of ghouls all over Tokyo.")
                   .setVolumes("14")
                   .setChapters(144)
                   .setPublished(LocalDate.of(2011,9,8))
                   .setGenres("Action, Adventure, Fantasy")
                   .setRating("Unknown")
                   .setAuthors("Ishida, Sui (Story & Art)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/3/114037.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());

           MangaEntity kaguyaSama = new MangaEntity()
                   .setTitle("Kaguya-sama wa Kokurasetai: Tensai-tachi no Renai Zunousen")
                   .setPopularity(25)
                   .setRanked(18)
                   .setMembers(452800)
                   .setContent("Considered a genius due to having the highest grades in the country, Miyuki Shirogane leads the prestigious Shuchiin Academy's student council as its president, working alongside the beautiful and wealthy vice president Kaguya Shinomiya. The two are often regarded as the perfect couple by students despite them not being in any sort of romantic relationship.\n" +
                           "\n" +
                           "However, the truth is that after spending so much time together, the two have developed feelings for one another; unfortunately, neither is willing to confess, as doing so would be a sign of weakness. With their pride as elite students on the line, Miyuki and Kaguya embark on a quest to do whatever is necessary to get a confession out of the other. Through their daily antics, the battle of love begins!")
                   .setVolumes("28")
                   .setChapters(281)
                   .setPublished(LocalDate.of(2015,5,19))
                   .setGenres("Award Winning, Comedy")
                   .setRating("Unknown")
                   .setAuthors("Akasaka, Aka (Story & Art)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/3/188896.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());

           MangaEntity classroomOfTheElite = new MangaEntity()
                   .setTitle("Youkoso Jitsuryoku Shijou Shugi no Kyoushitsu e")
                   .setPopularity(17)
                   .setRanked(17)
                   .setMembers(74092)
                   .setContent("Receiving funding from the government to nurture the next generation's hopefuls, Tokyo Metropolitan Advanced Nurturing High School brings together the brightest youth of Japan onto a single campus. At this seemingly perfect institution, the reserved Kiyotaka Ayanokouji arrives as an incoming member of class 1-D, where he befriends one of his classmates, the antisocial Suzune Horikita.\n" +
                           "\n" +
                           "At first, his peers revel in the academy's leisurely lifestyle, taking advantage of all of its state-of-the-art facilities. Soon enough, however, the facade of Tokyo Metropolitan Advanced Nurturing High School gives way to its true nature—only the top scoring classes can fully utilize the school's offerings, and Class D is the furthest from such a status. Standing at the bottom of the hierarchy, Class D houses all of the school's \"worst\" students.\n" +
                           "\n" +
                           "Following this rude awakening, Ayanokouji, Horikita, and the rest of Class D must overcome their differences and clash against other classes in order to climb to the coveted position of Class A by any means necessary.")
                   .setVolumes("14")
                   .setChapters(110)
                   .setPublished(LocalDate.of(2015,5,25))
                   .setGenres("Drama")
                   .setRating("Unknown")
                   .setAuthors("Tomose, Shunsaku (Art), Kinugasa, Shougo (Story)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/2/177958.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());


           MangaEntity deathNote = new MangaEntity()
                   .setTitle("Death Note")
                   .setPopularity(12)
                   .setRanked(46)
                   .setMembers(365344)
                   .setContent("Ryuk, a god of death, drops his Death Note into the human world for personal pleasure. In Japan, prodigious high school student Light Yagami stumbles upon it. Inside the notebook, he finds a chilling message: those whose names are written in it shall die. Its nonsensical nature amuses Light; but when he tests its power by writing the name of a criminal in it, they suddenly meet their demise.\n" +
                           "\n" +
                           "Realizing the Death Note's vast potential, Light commences a series of nefarious murders under the pseudonym \"Kira,\" vowing to cleanse the world of corrupt individuals and create a perfect society where crime ceases to exist. However, the police quickly catch on, and they enlist the help of L—a mastermind detective—to uncover the culprit.\n" +
                           "\n" +
                           "Death Note tells the thrilling tale of Light and L as they clash in a great battle-of-minds, one that will determine the future of the world.")
                   .setVolumes("12")
                   .setChapters(108)
                   .setPublished(LocalDate.of(2003,1,1))
                   .setGenres("Supernatural, Suspense")
                   .setRating("Unknown")
                   .setAuthors("Obata, Takeshi (Art), Ohba, Tsugumi (Story)")
                   .setImageUrl("https://cdn.myanimelist.net/images/manga/1/258245.jpg")
                   .setUser(admin)
                   .setStatusObject(StatusObjectEnum.ACCEPTED)
                   .setCreated(LocalDate.now());

           mangaRepository.saveAll(List.of(soloLeveling,onePiece,demonSlayer,tokyoGhoul,kaguyaSama,classroomOfTheElite,deathNote));

       }
   }
}
