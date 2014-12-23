#import "SIMProductViewController.h"
#import <Simplify/SIMChargeCardViewController.h>
#import <Simplify/SIMButton.h>
#import <Simplify/UIImage+Simplify.h>
#import <Simplify/UIColor+Simplify.h>
#import <Simplify/SIMResponseViewController.h>

//1. Sign up to be a SIMChargeViewControllerDelegate so that you get the callback that gives you a token
@interface SIMProductViewController ()<SIMChargeCardViewControllerDelegate>
@property (nonatomic, strong) SIMChargeCardViewController *chargeController;
@property (strong, nonatomic) IBOutlet SIMButton *buyButton;
@property (strong, nonatomic) UIColor *primaryColor;
@end

@implementation SIMProductViewController

-(void)testmenow
{
    //2. Create a SIMChargeViewController with your public api key
    SIMChargeCardViewController *chargeController = [[SIMChargeCardViewController alloc] initWithPublicKey:@"sbpb_ZmI0ODMyYjktZjE0MS00ZDcxLThmMGQtMDAzYzNlZmI4MDE5" primaryColor:self.primaryColor];
    
    //3. Assign your class as the delegate to the SIMChargeViewController class which takes the user input and requests a token
    chargeController.delegate = self;
    self.chargeController = chargeController;
    [self presentViewController:self.chargeController animated:YES completion:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    firstTime = YES;
    self.primaryColor = [UIColor colorWithRed:42.0/255.0 green:48.0/255.0 blue:145.0/255.0 alpha:1.0];
    self.buyButton.primaryColor = self.primaryColor;
    
}

- (void)viewDidAppear:(BOOL)animated
{
    
    if(firstTime){
        //2. Create a SIMChargeViewController with your public api key
        SIMChargeCardViewController *chargeController = [[SIMChargeCardViewController alloc] initWithPublicKey:@"sbpb_ZmI0ODMyYjktZjE0MS00ZDcxLThmMGQtMDAzYzNlZmI4MDE5" primaryColor:self.primaryColor];
        
        //3. Assign your class as the delegate to the SIMChargeViewController class which takes the user input and requests a token
        chargeController.delegate = self;
        self.chargeController = chargeController;
        [self presentViewController:self.chargeController animated:YES completion:nil];

        firstTime = NO;
    }
    
    
     
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - SIMChargeViewController Protocol
-(void)chargeCardCancelled {
    //User cancelled the SIMChargeCardViewController
    
    [self.chargeController dismissViewControllerAnimated:YES completion:nil];
    
    NSLog(@"User Cancelled");
}

-(void)creditCardTokenFailedWithError:(NSError *)error {
    
    //There was a problem generating the token
    
    NSLog(@"Credit Card Token Failed with error:%@", error.localizedDescription);
    UIImageView *blurredView = [UIImage blurImage:self.view.layer];
    SIMResponseViewController *viewController = [[SIMResponseViewController alloc] initWithBackground:blurredView primaryColor:self.primaryColor title:@"Failure." description:@"There was a problem with the payment.\nPlease try again."];
    [self presentViewController:viewController animated:YES completion:nil];
}

//5. This method will be called on your class whenever the user presses the Charge Card button and tokenization succeeds
-(void)creditCardTokenProcessed:(SIMCreditCardToken *)token {
    //Token was generated successfully, now you must use it
    
    NSURL *url= [NSURL URLWithString:@"https://localhost:8082/server.js"];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10.0];
    [request setHTTPMethod:@"POST"];
    NSString *postString = @"simplifyToken=";
    
    postString = [postString stringByAppendingString:token.token];
    
    [request setHTTPBody:[postString dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSError *error;
    
    //Process Request on your own server
    
    if (error) {
        NSLog(@"error:%@", error);
        UIImageView *blurredView = [UIImage blurImage:self.view.layer];
        SIMResponseViewController *viewController = [[SIMResponseViewController alloc] initWithBackground:blurredView primaryColor:self.primaryColor title:@"Failure." description:@"There was a problem with the payment.\nPlease try again."];
        [self presentViewController:viewController animated:YES completion:nil];
        
    } else {
        
        UIImageView *blurredView = [UIImage blurImage:self.view.layer];
        SIMResponseViewController *viewController = [[SIMResponseViewController alloc] initWithBackground:blurredView primaryColor:self.primaryColor title:@"Success!" description:@"Purchase Completed!"];
        [self presentViewController:viewController animated:YES completion:nil];
    }
    
}

@end
