@Service
public class Implement{
    public RecipeRepository recp;
    public UserRepository user;


    public Implement(RecipeRepository recp) {
        this.recp = recp;
    }

    public List<Recipe> getRecipeById(Long id){
        Optional<User> userDetail=user.findById(id);
        if(userDetail.isPresent()){
            return recp.findAllById(userDetail.get().getRecipeList());
        }else {
            throw new UserNotFoundException();
        }

    }

}