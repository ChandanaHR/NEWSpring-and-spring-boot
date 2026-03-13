//Text field handling
<form action="/saveName" method="post">
Name:
<input type="text" name="name">
<input type="submit">
</form>
@PostMapping("/saveName")
@ResponseBody
public String saveName(@RequestParam String name) {

    return "Name received: " + name;
}
